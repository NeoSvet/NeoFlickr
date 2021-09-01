package ru.neosvet.flickr.photo

import ru.neosvet.flickr.api.Client
import javax.inject.Inject

class PhotoSource @Inject constructor(
    private val api: Client
) : IPhotoSource {
    override fun getSizes(photo_id: String) = api.getSizes(photo_id)
    override fun getInfo(photo_id: String) = api.getInfo(photo_id)
}