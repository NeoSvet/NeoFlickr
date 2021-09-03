package ru.neosvet.flickr.gallery

import io.reactivex.rxjava3.core.Observable
import ru.neosvet.flickr.entities.GalleryItem
import ru.neosvet.flickr.entities.PhotoItem

interface IGallerySource {
    val currentPage: Int
    val currentPages: Int
    fun getListIds(photos: String): List<String>
    fun getPhotos(gallery: GalleryItem): Observable<List<PhotoItem>>
    fun getPopular(userId: String, page: Int): Observable<GalleryItem>
    fun getGallery(galleryId: String, page: Int): Observable<GalleryItem>
    fun getSearch(query: String, page: Int): Observable<GalleryItem>
}