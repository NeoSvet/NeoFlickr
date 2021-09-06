package ru.neosvet.flickr.photo

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.neosvet.flickr.api.Client
import ru.neosvet.flickr.entities.*
import ru.neosvet.flickr.scheduler.Schedulers
import ru.neosvet.flickr.storage.FlickrStorage
import java.util.*
import javax.inject.Inject

class PhotoSource @Inject constructor(
    private val schedulers: Schedulers,
    private val api: Client,
    private val storage: FlickrStorage
) : IPhotoSource {
    private var currentId = ""

    override fun getUrlBig(photoId: String): Single<String> {
        currentId = photoId

        return storage.photoDao.get(photoId).flatMap {
            it.urlBig?.let {
                Single.fromCallable { it }
            } ?: api.getSizes(photoId)
                .map {
                    if (it.stat.equals("fail"))
                        throw Exception(it.message)
                    it
                }
                .map(this::parseSizes)
        }
    }

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
            if (max.width < it.width && it.label != "Original")
                max = it
        }
        return max.source
    }

    override fun getInfo(photoId: String): Observable<InfoItem> = Observable.merge(
        storage.infoDao.get(photoId),
        loadInfo(photoId)
    )

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