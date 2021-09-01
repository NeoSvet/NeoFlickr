package ru.neosvet.flickr.gallery

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import ru.neosvet.flickr.entities.Photo
import ru.neosvet.flickr.entities.PhotoItem
import ru.neosvet.flickr.entities.PhotosResponse
import ru.neosvet.flickr.scheduler.Schedulers
import ru.neosvet.flickr.screens.PhotoScreen
import ru.neosvet.flickr.screens.SettingsScreen
import ru.neosvet.flickr.utils.GalleryType
import ru.neosvet.flickr.utils.ISettings

class GalleryPresenter(
    private val source: IGallerySource,
    private val settings: ISettings,
    private val router: Router,
    private val schedulers: Schedulers
) : MvpPresenter<GalleryView>() {
    var query: String? = null
        private set

    class GalleryListPresenter(private val router: Router) : IGalleryListPresenter {
        val photos = mutableListOf<PhotoItem>()
        override var itemClickListener: ((IGalleryItemView) -> Unit)? = { item ->
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
        else
            viewState.showLoading()
    }

    override fun onDestroy() {
        process?.dispose()
    }

    private fun loadPopular(page: Int) {
        query = null
        process = source.getPopular(settings.getUserId(), page)
            .observeOn(schedulers.main())
            .subscribeOn(schedulers.background())
            .map {
                if (it.stat.equals("fail"))
                    throw Exception(it.message)
                it
            }
            .subscribe(
                this::parseResponse,
                viewState::showError
            )
    }

    private fun loadGallery(page: Int) {
        query = null
        process = source.getGallery(settings.getGalleryId(), page)
            .observeOn(schedulers.main())
            .subscribeOn(schedulers.background())
            .map {
                if (it.stat.equals("fail"))
                    throw Exception(it.message)
                it
            }
            .subscribe(
                this::parseResponse,
                viewState::showError
            )
    }

    fun search(query: String, page: Int = 1) {
        startLoading()
        this.query = query
        process = source.searchImages(query, page)
            .observeOn(schedulers.main())
            .subscribeOn(schedulers.background())
            .map {
                if (it.stat.equals("fail"))
                    throw Exception(it.message)
                it
            }
            .subscribe(
                this::parseResponse,
                viewState::showError
            )
    }

    private fun parseResponse(response: PhotosResponse) {
        val list = response.photos?.photo?.map {
            PhotoItem(
                id = it.id,
                owner = it.owner,
                url = getUrl(it),
                title = it.title
            )
        }

        if (list != null) {
            galleryListPresenter.photos.clear()
            galleryListPresenter.photos.addAll(list)
            response.photos?.let {
                viewState.updateList(it.page, it.pages)
            }
        }
    }

    private fun getUrl(photo: Photo) =
        "https://live.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_w.jpg"
    //see more https://www.flickr.com/services/api/misc.urls.html

    fun openSettings() {
        router.replaceScreen(SettingsScreen.create())
    }

    fun changePage(page: Int) {
        query?.let {
            search(it, page)
        } ?: loadHome(page)
    }
}