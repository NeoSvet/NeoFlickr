package ru.neosvet.flickr.gallery

import io.reactivex.rxjava3.core.Single
import ru.neosvet.flickr.entities.PhotosResponse

interface IGallerySource {
    fun getPopular(user_id: String, page: Int): Single<PhotosResponse>
    fun getGallery(gallery_id: String, page: Int): Single<PhotosResponse>
    fun searchImages(query: String, page: Int): Single<PhotosResponse>
}