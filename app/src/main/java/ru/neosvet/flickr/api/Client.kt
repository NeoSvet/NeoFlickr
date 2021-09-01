package ru.neosvet.flickr.api

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.neosvet.flickr.FLICKR_API_KEY
import ru.neosvet.flickr.USER_ID
import ru.neosvet.flickr.entities.PhotosResponse

interface Client {
    @GET("?method=flickr.photos.getPopular&format=json&nojsoncallback=1&user_id=$USER_ID&api_key=$FLICKR_API_KEY")
    fun getPopular(): Single<PhotosResponse>

    @GET("?method=flickr.galleries.getPhotos&format=json&nojsoncallback=1&api_key=$FLICKR_API_KEY")
    fun getPhotos(
        @Query("gallery_id") id: String
    ): Single<PhotosResponse>

    @GET("?method=flickr.photos.search&format=json&nojsoncallback=1&api_key=$FLICKR_API_KEY")
    fun searchImages(
        @Query("text") query: String
    ): Single<PhotosResponse>
}