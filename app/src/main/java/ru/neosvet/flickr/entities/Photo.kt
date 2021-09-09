package ru.neosvet.flickr.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class PhotosResponse(
    @SerializedName("photos") val photos: Photos?,
    @SerializedName("stat") val stat: String,
    @SerializedName("message") val message: String?
)

data class Photos(
    @SerializedName("page") val page: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("perpage") val perpage: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("photo") val photo: List<Photo>
)

data class Photo(
    @SerializedName("id") val id: String,
    @SerializedName("owner") val owner: String,
    @SerializedName("secret") val secret: String,
    @SerializedName("server") val server: String,
    @SerializedName("farm") val farm: Int,
    @SerializedName("title") val title: String,
    @SerializedName("ispublic") val ispublic: Int,
    @SerializedName("isfriend") val isfriend: Int,
    @SerializedName("isfamily") val isfamily: Int,
    @SerializedName("is_primary") val isPrimary: Int,
    @SerializedName("has_comment") val hasComment: Int
)

@Entity(tableName = "Photo")
data class PhotoItem(
    @PrimaryKey val id: String,
    val owner: String,
    val urlMini: String,
    val urlBig: String?,
    val title: String
)

