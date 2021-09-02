package ru.neosvet.flickr.photo

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.neosvet.flickr.entities.InfoItem
import ru.neosvet.flickr.entities.InfoResponse
import ru.neosvet.flickr.entities.SizesResponse

interface IPhotoSource {
    fun getUrlBig(photoId: String): Single<String>
    fun getInfo(photoId: String): Observable<InfoItem>
}