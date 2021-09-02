package ru.neosvet.flickr.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class PhotosResponse(
    @SerializedName("photos") var photos: Photos?,
    @SerializedName("stat") var stat: String,
    @SerializedName("message") var message: String?
)

data class Photos(
    @SerializedName("page") var page: Int,
    @SerializedName("pages") var pages: Int,
    @SerializedName("perpage") var perpage: Int,
    @SerializedName("total") var total: Int,
    @SerializedName("photo") var photo: List<Photo>
)

data class Photo(
    @SerializedName("id") var id: String,
    @SerializedName("owner") var owner: String,
    @SerializedName("secret") var secret: String,
    @SerializedName("server") var server: String,
    @SerializedName("farm") var farm: Int,
    @SerializedName("title") var title: String,
    @SerializedName("ispublic") var ispublic: Int,
    @SerializedName("isfriend") var isfriend: Int,
    @SerializedName("isfamily") var isfamily: Int,
    @SerializedName("is_primary") var isPrimary: Int,
    @SerializedName("has_comment") var hasComment: Int
)

@Entity
data class PhotoItem(
    @PrimaryKey val id: String,
    val owner: String,
    val urlMini: String,
    var urlBig: String?,
    var title: String
)

