package ru.neosvet.flickr.gallery

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import ru.neosvet.flickr.entities.Photo
import ru.neosvet.flickr.entities.PhotoItem
import ru.neosvet.flickr.scheduler.Schedulers

class GalleryPresenter(
    private val source: IGallerySource,
    private val router: Router,
    private val schedulers: Schedulers
) : MvpPresenter<GalleryView>() {

    class GalleryListPresenter : IGalleryListPresenter {
        val photos = mutableListOf<PhotoItem>()
        override var itemClickListener: ((IGalleryItemView) -> Unit)? = null

        override fun bindView(view: IGalleryItemView) {
            val photo = photos[view.pos]
            view.setPhoto(photo)
        }

        override fun getCount() = photos.size
    }

    val galleryListPresenter = GalleryListPresenter()
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
            .map {
                it.photos.photo.map {
                    PhotoItem(
                        id = it.id,
                        owner = it.owner,
                        url = getUrl(it),
                        title = it.title
                    )
                }
            }
            .subscribe({
                println("Photos: " + it)
                galleryListPresenter.photos.addAll(it)
                viewState.updateList()
            }, {
                println("Error: " + it.message)
                viewState.showError(it)
            })
    }

    private fun getUrl(photo: Photo) =
        "https://live.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_w.jpg"
    //see more https://www.flickr.com/services/api/misc.urls.html

    fun back(): Boolean {
        router.exit()
        return true
    }
}