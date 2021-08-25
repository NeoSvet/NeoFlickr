package ru.neosvet.flickr.gallery

import io.reactivex.rxjava3.core.Single
import ru.neosvet.flickr.entities.PhotosResponse

interface IGallerySource {
    fun getPopular(): Single<PhotosResponse>
    fun getPhotos(gallery_id: String): Single<PhotosResponse>
    fun searchImages(query: String): Single<PhotosResponse>
}