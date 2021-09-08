package ru.neosvet.flickr.photo

import io.reactivex.rxjava3.core.Observable
import ru.neosvet.android4.mvp.model.network.INetworkStatus
import ru.neosvet.flickr.api.Client
import ru.neosvet.flickr.entities.*
import ru.neosvet.flickr.scheduler.Schedulers
import ru.neosvet.flickr.storage.FlickrStorage
import java.util.*
import javax.inject.Inject

class PhotoSource @Inject constructor(
    private val schedulers: Schedulers,
    private val api: Client,
    private val storage: FlickrStorage,
    private val networkStatus: INetworkStatus
) : IPhotoSource {
    private var currentId = ""

    override fun getUrlBig(photoId: String): Observable<String> {
        currentId = photoId

        return networkStatus.isOnline().flatMap { isOnline ->
            if (isOnline) {
                Observable.merge(
                    getSize(photoId),
                    loadSize(photoId)
                )
            } else
                getSize(photoId)
        }
    }

    private fun getSize(photoId: String) =
        storage.photoDao.get(photoId).map {
            it.urlBig ?: ""
        }.toObservable()

    private fun loadSize(photoId: String): Observable<String> =
        api.getSizes(photoId)
            .map {
                if (it.stat.equals("fail"))
                    throw Exception(it.message)
                it
            }
            .map(this::parseSizes)
            .toObservable()

    private fun parseSizes(response: SizesResponse): String {
        response.sizes?.size?.let {
            val url = findUrlBiggest(it)
            storage.photoDao.updateUrlBig(currentId, url)
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe()
            return url
        }
        return ""
    }

    private fun findUrlBiggest(list: List<Size>): String {
        var max = list[0]
        list.forEach {
            if (it.label != "Original" && it.label != "Video Player") {
                if (max.media != it.media && it.media == "video")
                    max = it
                else if (max.width < it.width)
                    max = it
            }
        }
        return max.source
    }

    override fun getInfo(photoId: String): Observable<InfoItem> =
        networkStatus.isOnline().flatMap { isOnline ->
            if (isOnline) {
                Observable.merge(
                    storage.infoDao.get(photoId),
                    loadInfo(photoId)
                )
            } else
                storage.infoDao.get(photoId)
        }

    private fun loadInfo(photoId: String): Observable<InfoItem> =
        api.getInfo(photoId)
            .map {
                if (it.stat.equals("fail"))
                    throw Exception(it.message)
                it
            }
            .map(this::parseInfo)
            .toObservable()

    private fun parseInfo(response: InfoResponse): InfoItem =
        response.photo?.let {
            val info = InfoItem(
                photoId = it.id,
                owner = getOwnerName(it.owner),
                date = dateToString(it.dates.posted),
                title = it.title.content,
                description = it.description.content
            )

            storage.infoDao.insert(info)
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .subscribe()

            info
        } ?: throw NullPointerException()

    private fun getOwnerName(owner: Owner) = if (owner.realname.isNotEmpty())
        "${owner.username} (${owner.realname})"
    else
        owner.username

    private fun dateToString(value: String): String {
        val date = Date(value.toLong() * 1000)
        return date.toLocaleString()
    }
}