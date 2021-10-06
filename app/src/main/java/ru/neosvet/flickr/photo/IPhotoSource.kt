package ru.neosvet.flickr.photo

import io.reactivex.rxjava3.core.Observable
import ru.neosvet.flickr.entities.InfoItem

interface IPhotoSource {
    fun getUrlBig(photoId: String): Observable<String>
    fun getInfo(photoId: String): Observable<InfoItem>
}