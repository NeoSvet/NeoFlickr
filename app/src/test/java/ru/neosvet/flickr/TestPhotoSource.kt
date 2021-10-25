package ru.neosvet.flickr

import io.reactivex.rxjava3.core.Observable
import ru.neosvet.flickr.entities.InfoItem
import ru.neosvet.flickr.photo.IPhotoSource

class TestPhotoSource : IPhotoSource {
    override fun getUrlBig(photoId: String): Observable<String> =
        Observable.just("https://live.staticflickr.com/7335/26397793043_8071597b58_c.jpg")


    override fun getInfo(photoId: String): Observable<InfoItem> =
        Observable.just(
            InfoItem(
                photoId = photoId,
                owner = "Raine Photos (Julien Raine)",
                date = "14 мая 2016 г. 9:30:39 AM",
                title = "Moineau domestique - {0160418}#{32101}",
                description = ""
            )
        )
}