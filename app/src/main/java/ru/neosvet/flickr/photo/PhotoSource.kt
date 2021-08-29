package ru.neosvet.flickr.photo

import io.reactivex.rxjava3.core.Single
import ru.neosvet.flickr.api.Client
import ru.neosvet.flickr.entities.SizesResponse
import javax.inject.Inject

class PhotoSource @Inject constructor(
    private val api: Client
) : IPhotoSource {
    override fun getSizes(photo_id: String) = api.getSizes(photo_id)
    override fun getInfo(photo_id: String) = api.getInfo(photo_id)
}