package ru.neosvet.flickr.gallery

import ru.neosvet.flickr.api.Client
import javax.inject.Inject

class GallerySource @Inject constructor(
    private val api: Client
) : IGallerySource {

    override fun getPopular() = api.getPopular()

    override fun getPhotos(gallery_id: String) = api.getPhotos(gallery_id)

    override fun searchImages(query: String) = api.searchImages(query)
}