package ru.neosvet.flickr.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class InfoResponse (
    @SerializedName("photo") val photo : PhotoInfo?,
    @SerializedName("stat") val stat : String,
    @SerializedName("message") val message: String?
)

data class PhotoInfo (
    @SerializedName("id") val id : String,
    @SerializedName("secret") val secret : String,
    @SerializedName("server") val server : String,
    @SerializedName("farm") val farm : Int,
    @SerializedName("dateuploaded") val dateuploaded : String,
    @SerializedName("isfavorite") val isfavorite : Int,
    @SerializedName("license") val license : String,
    @SerializedName("safety_level") val safetyLevel : String,
    @SerializedName("rotation") val rotation : Int,
    @SerializedName("owner") val owner : Owner,
    @SerializedName("title") val title : StringContent,
    @SerializedName("description") val description : StringContent,
    @SerializedName("visibility") val visibility : Visibility,
    @SerializedName("dates") val dates : Dates,
    @SerializedName("views") val views : String,
    @SerializedName("editability") val editability : Canability,
    @SerializedName("publiceditability") val publiceditability : Canability,
    @SerializedName("usage") val usage : Usage,
    @SerializedName("comments") val comments : StringContent,
    @SerializedName("notes") val notes : Notes,
    @SerializedName("people") val people : People,
    @SerializedName("tags") val tags : Tags,
    @SerializedName("location") val location : Location,
    @SerializedName("geoperms") val geoperms : Geoperms,
    @SerializedName("urls") val urls : Urls,
    @SerializedName("media") val media : String
)

data class Owner (
    @SerializedName("nsid") val nsid : String,
    @SerializedName("username") val username : String,
    @SerializedName("realname") val realname : String,
    @SerializedName("location") val location : String,
    @SerializedName("iconserver") val iconserver : String,
    @SerializedName("iconfarm") val iconfarm : Int,
    @SerializedName("path_alias") val pathAlias : String
)

data class StringContent (
    @SerializedName("_content") val content : String
)

data class Visibility (
    @SerializedName("ispublic") val ispublic : Int,
    @SerializedName("isfriend") val isfriend : Int,
    @SerializedName("isfamily") val isfamily : Int
)

data class Dates (
    @SerializedName("posted") val posted : String,
    @SerializedName("taken") val taken : String,
    @SerializedName("takengranularity") val takengranularity : String,
    @SerializedName("takenunknown") val takenunknown : String,
    @SerializedName("lastupdate") val lastupdate : String
)

data class Canability (
    @SerializedName("cancomment") val cancomment : Int,
    @SerializedName("canaddmeta") val canaddmeta : Int
)

data class Usage (
    @SerializedName("candownload") val candownload : Int,
    @SerializedName("canblog") val canblog : Int,
    @SerializedName("canprint") val canprint : Int,
    @SerializedName("canshare") val canshare : Int
)

data class Notes (
    @SerializedName("note") val note : List<String>
)

data class People (
    @SerializedName("haspeople") val haspeople : Int
)

data class Tag (
    @SerializedName("id") val id : String,
    @SerializedName("author") val author : String,
    @SerializedName("authorname") val authorname : String,
    @SerializedName("raw") val raw : String,
    @SerializedName("_content") val content : String,
    //@SerializedName("machine_tag") val machineTag : Int or Boolean
)

data class Tags (
    @SerializedName("tag") val tag : List<Tag>
)

data class Location (
    @SerializedName("latitude") val latitude : String,
    @SerializedName("longitude") val longitude : String,
    @SerializedName("accuracy") val accuracy : String,
    @SerializedName("context") val context : String,
    @SerializedName("locality") val locality : StringContent,
    @SerializedName("county") val county : StringContent,
    @SerializedName("region") val region : StringContent,
    @SerializedName("country") val country : StringContent,
    @SerializedName("neighbourhood") val neighbourhood : StringContent
)

data class Geoperms (
    @SerializedName("ispublic") val ispublic : Int,
    @SerializedName("iscontact") val iscontact : Int,
    @SerializedName("isfriend") val isfriend : Int,
    @SerializedName("isfamily") val isfamily : Int
)

data class Url (
    @SerializedName("type") val type : String,
    @SerializedName("_content") val content : String
)

data class Urls (
    @SerializedName("url") val url : List<Url>
)

@Entity(
    tableName = "Info",
    foreignKeys = [ForeignKey(
        entity = PhotoItem::class,
        parentColumns = ["id"],
        childColumns = ["photoId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class InfoItem(
    @PrimaryKey val photoId: String,
    val owner: String,
    val date: String,
    val title: String,
    val description: String
)