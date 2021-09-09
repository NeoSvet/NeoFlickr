package ru.neosvet.flickr.gallery

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.neosvet.android4.mvp.model.network.INetworkStatus
import ru.neosvet.flickr.api.Client
import ru.neosvet.flickr.entities.GalleryItem
import ru.neosvet.flickr.entities.Photo
import ru.neosvet.flickr.entities.PhotoItem
import ru.neosvet.flickr.entities.PhotosResponse
import ru.neosvet.flickr.scheduler.Schedulers
import ru.neosvet.flickr.storage.FlickrStorage
import javax.inject.Inject

class GallerySource @Inject constructor(
    private val schedulers: Schedulers,
    private val api: Client,
    private val storage: FlickrStorage,
    private val networkStatus: INetworkStatus
) : IGallerySource {
    private val delimiter = "@"
    private val SEARCH = "search:"
    private val PAGE = "page:"
    private var currentName = "0"

    override var currentPage = 1
        private set
    override var currentPages = 1
        private set

    override fun getListIds(photos: String) = photos.split(delimiter)

    override fun getPhotos(gallery: GalleryItem): Observable<List<PhotoItem>> {
        currentPage = gallery.page
        currentPages = gallery.pages

        return storage.photoDao.get(getListIds(gallery.photos))
    }

    override fun getPopular(userId: String, page: Int): Observable<GalleryItem> {
        currentName = userId + PAGE + page

        return networkStatus.isOnline().flatMap { isOnline ->
            if (isOnline) {
                Observable.merge(
                    storage.galleryDao.get(currentName, page),
                    loadPopular(userId, page)
                )
            } else
                storage.galleryDao.get(currentName, page)
        }
    }

    private fun loadPopular(userId: String, page: Int): Observable<GalleryItem> =
        api.getPopular(userId, page)
            .flatMap(this::checkFail)
            .map(this::parseResponse)
            .toObservable()

    override fun getGallery(galleryId: String, page: Int): Observable<GalleryItem> {
        currentName = galleryId + PAGE + page

        return networkStatus.isOnline().flatMap { isOnline ->
            if (isOnline) {
                Observable.merge(
                    storage.galleryDao.get(currentName, page),
                    loadGallery(galleryId, page)
                )
            } else
                storage.galleryDao.get(currentName, page)
        }
    }

    private fun loadGallery(galleryId: String, page: Int): Observable<GalleryItem> =
        api.getGallery(galleryId, page)
            .flatMap(this::checkFail)
            .map(this::parseResponse)
            .toObservable()

    override fun getSearch(query: String, page: Int): Observable<GalleryItem> {
        currentName = SEARCH + query + PAGE + page

        return Observable.merge(
            storage.galleryDao.get(currentName, page),
            loadSearch(query, page)
        )
    }

    private fun loadSearch(query: String, page: Int): Observable<GalleryItem> =
        api.searchImages(query, page)
            .flatMap(this::checkFail)
            .map(this::parseResponse)
            .toObservable()

    private fun checkFail(response: PhotosResponse): Single<PhotosResponse> =
        if (response.stat == "fail")
            Single.error(Exception(response.message))
        else
            Single.just(response)

    private fun parseResponse(response: PhotosResponse): GalleryItem {
        val photoIds = StringBuilder()

        response.photos?.photo?.forEach {
            photoIds.append(delimiter)
            photoIds.append(it.id)

            storage.photoDao.insert(
                PhotoItem(
                    id = it.id,
                    owner = it.owner,
                    urlMini = getUrl(it),
                    urlBig = null,
                    title = it.title
                )
            )
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe()
        }

        if (photoIds.isNotEmpty())
            photoIds.delete(0, 1)

        val gallery = GalleryItem(
            name = currentName,
            page = response.photos?.page ?: 1,
            pages = response.photos?.pages ?: 1,
            photos = photoIds.toString()
        )

        storage.galleryDao.insert(gallery)
            .observeOn(schedulers.main())
            .subscribeOn(schedulers.background())
            .subscribe()

        return gallery
    }

    private fun getUrl(photo: Photo) =
        "https://live.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_w.jpg"
    //see more https://www.flickr.com/services/api/misc.urls.html
}

