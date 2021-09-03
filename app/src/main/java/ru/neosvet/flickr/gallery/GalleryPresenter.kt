package ru.neosvet.flickr.gallery

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import ru.neosvet.flickr.entities.GalleryItem
import ru.neosvet.flickr.entities.PhotoItem
import ru.neosvet.flickr.scheduler.Schedulers
import ru.neosvet.flickr.screens.PhotoScreen
import ru.neosvet.flickr.screens.SettingsScreen
import ru.neosvet.flickr.settings.GalleryType
import ru.neosvet.flickr.settings.ISettings

class GalleryPresenter(
    private val source: IGallerySource,
    private val settings: ISettings,
    private val router: Router,
    private val schedulers: Schedulers
) : MvpPresenter<GalleryView>() {
    var query: String? = null
        private set
    private var orderIds: List<String>? = null

    class GalleryListPresenter(private val router: Router) : IGalleryListPresenter {
        val photos = mutableListOf<PhotoItem>()
        var lastClickedPos: Int? = null

        override var itemClickListener: ((IGalleryItemView) -> Unit)? = { item ->
            lastClickedPos = item.pos
            val photo = photos[item.pos]
            router.navigateTo(PhotoScreen.create(photo.id))
        }

        override fun bindView(view: IGalleryItemView) {
            val photo = photos[view.pos]
            view.setPhoto(photo)
        }

        override fun getCount() = photos.size
    }

    val galleryListPresenter = GalleryListPresenter(router)
    private var process: Disposable? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        loadHome()
    }

    fun loadHome(page: Int = 1) {
        startLoading()
        when (settings.getGalleryType()) {
            GalleryType.Popular -> loadPopular(page)
            GalleryType.Gallery -> loadGallery(page)
        }
    }

    private fun startLoading() {
        if (process?.isDisposed == false)
            process?.dispose()

        viewState.showLoading()
    }

    override fun onDestroy() {
        process?.dispose()
    }

    private fun loadPopular(page: Int) {
        query = null
        process = source.getPopular(settings.getUserId(), page)
            .subscribeOn(schedulers.background())
            .flatMap(this::getPhotos)
            .observeOn(schedulers.main())
            .subscribe(
                this::putPhotos,
                viewState::showError
            )
    }

    private fun loadGallery(page: Int) {
        query = null
        process = source.getGallery(settings.getGalleryId(), page)
            .subscribeOn(schedulers.background())
            .flatMap(this::getPhotos)
            .observeOn(schedulers.main())
            .subscribe(
                this::putPhotos,
                viewState::showError
            )
    }

    fun search(query: String, page: Int = 1) {
        startLoading()
        this.query = query
        process = source.getSearch(query, page)
            .subscribeOn(schedulers.background())
            .flatMap(this::getPhotos)
            .observeOn(schedulers.main())
            .subscribe(
                this::putPhotos,
                viewState::showError
            )
    }

    private fun getPhotos(gallery: GalleryItem): Observable<List<PhotoItem>> {
        orderIds = source.getListIds(gallery.photos)

        return source.getPhotos(gallery)
    }

    private fun getList() = galleryListPresenter.photos

    private fun putPhotos(list: List<PhotoItem>) {
        viewState.updatePages(source.currentPage, source.currentPages)

        val sortedList = sortPhotos(list)
        if (getList() == sortedList)
            return
        getList().clear()
        getList().addAll(sortedList)
        viewState.updateGallery()
    }

    private fun sortPhotos(list: List<PhotoItem>): List<PhotoItem> {
        return orderIds?.let { ids ->
            val newList = list.toMutableList()
            list.forEach {
                val n = ids.indexOf(it.id)
                newList[n] = it
            }
            newList
        } ?: list
    }

    fun openSettings() {
        router.replaceScreen(SettingsScreen.create())
    }

    fun changePage(page: Int) {
        query?.let {
            search(it, page)
        } ?: loadHome(page)
    }
}