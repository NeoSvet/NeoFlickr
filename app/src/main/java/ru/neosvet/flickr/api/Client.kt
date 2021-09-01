package ru.neosvet.flickr.api

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.neosvet.flickr.entities.*

interface Client {
    @GET("?method=flickr.urls.lookupGallery")
    fun findGallery(
        @Query("url") url: String
    ): Single<GalleryResponse>

    @GET("?method=flickr.people.findByUsername")
    fun findUser(
        @Query("username") username: String
    ): Single<UserResponse>

    @GET("?method=flickr.photos.getPopular&per_page=30")
    fun getPopular(
        @Query("user_id") user_id: String,
        @Query("page") page: Int
    ): Single<PhotosResponse>

    @GET("?method=flickr.photos.getSizes")
    fun getSizes(
        @Query("photo_id") photo_id: String
    ): Single<SizesResponse>

    @GET("?method=flickr.photos.getInfo")
    fun getInfo(
        @Query("photo_id") photo_id: String
    ): Single<InfoResponse>

    @GET("?method=flickr.galleries.getPhotos&per_page=30")
    fun getGallery(
        @Query("gallery_id") gallery_id: String,
        @Query("page") page: Int
    ): Single<PhotosResponse>

    @GET("?method=flickr.photos.search&per_page=30")
    fun searchImages(
        @Query("text") query: String,
        @Query("page") page: Int
    ): Single<PhotosResponse>
}