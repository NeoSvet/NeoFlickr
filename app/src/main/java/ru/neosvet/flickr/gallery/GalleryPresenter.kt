package ru.neosvet.flickr.gallery

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import ru.neosvet.flickr.entities.Photo
import ru.neosvet.flickr.entities.PhotoItem
import ru.neosvet.flickr.entities.PhotosResponse
import ru.neosvet.flickr.scheduler.Schedulers
import ru.neosvet.flickr.screens.PhotoScreen

class GalleryPresenter(
    private val source: IGallerySource,
    private val router: Router,
    private val schedulers: Schedulers
) : MvpPresenter<GalleryView>() {

    class GalleryListPresenter(private val router: Router) : IGalleryListPresenter {
        val photos = mutableListOf<PhotoItem>()
        override var itemClickListener: ((IGalleryItemView) -> Unit)? = { item ->
            val photo = photos[item.pos]
            router.navigateTo(PhotoScreen.create(photo))
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
        loadData()
    }

    override fun onDestroy() {
        process?.dispose()
    }

    private fun loadData() {
        process = source.getPopular()
            .observeOn(schedulers.main())
            .subscribeOn(schedulers.background())
            .subscribe(
                this::parseResponse,
                viewState::showError
            )
    }

    private fun parseResponse(response: PhotosResponse) {
        if (response.stat.equals("fail"))
            throw Exception(response.message)

        val list = response.photos?.photo?.map {
            PhotoItem(
                id = it.id,
                owner = it.owner,
                url = getUrl(it),
                title = it.title
            )
        }

        if (list != null) {
            galleryListPresenter.photos.addAll(list)
            viewState.updateList()
        }
    }

    private fun getUrl(photo: Photo) =
        "https://live.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}"
    //see more https://www.flickr.com/services/api/misc.urls.html

    fun back(): Boolean {
        router.exit()
        return true
    }
}