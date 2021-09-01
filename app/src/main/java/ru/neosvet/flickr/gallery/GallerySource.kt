package ru.neosvet.flickr.gallery

import ru.neosvet.flickr.api.Client
import javax.inject.Inject

class GallerySource @Inject constructor(
    private val api: Client
) : IGallerySource {
    override fun getPopular(user_id: String, page: Int) = api.getPopular(user_id, page)
    override fun getGallery(gallery_id: String, page: Int) = api.getGallery(gallery_id, page)
    override fun searchImages(query: String, page: Int) = api.searchImages(query, page)
}

