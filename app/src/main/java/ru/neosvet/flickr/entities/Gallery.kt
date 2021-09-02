package ru.neosvet.flickr.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class GalleryResponse(
    @SerializedName("gallery") var gallery: Gallery?,
    @SerializedName("stat") var stat: String,
    @SerializedName("message") var message: String?
)

data class Gallery(
    @SerializedName("id") var id: String,
    @SerializedName("gallery_id") var galleryId: String,
    @SerializedName("url") var url: String,
    @SerializedName("owner") var owner: String,
    @SerializedName("username") var username: String,
    @SerializedName("iconserver") var iconserver: String,
    @SerializedName("iconfarm") var iconfarm: Int,
    @SerializedName("primary_photo_id") var primaryPhotoId: String,
    @SerializedName("date_create") var dateCreate: String,
    @SerializedName("date_update") var dateUpdate: String,
    @SerializedName("count_photos") var countPhotos: Int,
    @SerializedName("count_videos") var countVideos: Int,
    @SerializedName("count_total") var countTotal: Int,
    @SerializedName("count_views") var countViews: Int,
    @SerializedName("count_comments") var countComments: Int,
    @SerializedName("title") var title: StringContent,
    @SerializedName("description") var description: StringContent,
    @SerializedName("sort_group") var sortGroup: String,
    @SerializedName("cover_photos") var coverPhotos: CoverPhotos,
    @SerializedName("current_state") var currentState: String,
    @SerializedName("primary_photo_server") var primaryPhotoServer: String,
    @SerializedName("primary_photo_farm") var primaryPhotoFarm: Int,
    @SerializedName("primary_photo_secret") var primaryPhotoSecret: String
)

data class CoverPhotos(
    @SerializedName("photo") var photo: List<CoverPhoto>
)

data class CoverPhoto(
    @SerializedName("url") var url: String,
    @SerializedName("width") var width: Int,
    @SerializedName("height") var height: Int,
    @SerializedName("is_primary") var isPrimary: Int,
    @SerializedName("is_video") var isVideo: Int
)

@Entity
data class GalleryItem(
    @PrimaryKey val name: String,
    val page: Int,
    val pages: Int,
    val photos: String
)
