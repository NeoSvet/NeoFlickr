package ru.neosvet.flickr.photo

import io.reactivex.rxjava3.core.Single
import ru.neosvet.flickr.entities.InfoResponse
import ru.neosvet.flickr.entities.SizesResponse

interface IPhotoSource {
    fun getSizes(photo_id: String): Single<SizesResponse>
    fun getInfo(photo_id: String): Single<InfoResponse>
}