package ru.neosvet.flickr.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class GalleryResponse(
    @SerializedName("gallery") val gallery: Gallery?,
    @SerializedName("stat") val stat: String,
    @SerializedName("message") val message: String?
)

data class Gallery(
    @SerializedName("id") val id: String,
    @SerializedName("gallery_id") val galleryId: String,
    @SerializedName("url") val url: String,
    @SerializedName("owner") val owner: String,
    @SerializedName("username") val username: String,
    @SerializedName("iconserver") val iconserver: String,
    @SerializedName("iconfarm") val iconfarm: Int,
    @SerializedName("primary_photo_id") val primaryPhotoId: String,
    @SerializedName("date_create") val dateCreate: String,
    @SerializedName("date_update") val dateUpdate: String,
    @SerializedName("count_photos") val countPhotos: Int,
    @SerializedName("count_videos") val countVideos: Int,
    @SerializedName("count_total") val countTotal: Int,
    @SerializedName("count_views") val countViews: Int,
    @SerializedName("count_comments") val countComments: Int,
    @SerializedName("title") val title: StringContent,
    @SerializedName("description") val description: StringContent,
    @SerializedName("sort_group") val sortGroup: String,
    @SerializedName("cover_photos") val coverPhotos: CoverPhotos,
    @SerializedName("current_state") val currentState: String,
    @SerializedName("primary_photo_server") val primaryPhotoServer: String,
    @SerializedName("primary_photo_farm") val primaryPhotoFarm: Int,
    @SerializedName("primary_photo_secret") val primaryPhotoSecret: String
)

data class CoverPhotos(
    @SerializedName("photo") val photo: List<CoverPhoto>
)

data class CoverPhoto(
    @SerializedName("url") val url: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("is_primary") val isPrimary: Int,
    @SerializedName("is_video") val isVideo: Int
)

@Entity(tableName = "Gallery")
data class GalleryItem(
    @PrimaryKey val name: String,
    val page: Int,
    val pages: Int,
    val photos: String
)
