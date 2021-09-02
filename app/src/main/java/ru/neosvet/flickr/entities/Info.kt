package ru.neosvet.flickr.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class InfoResponse (
    @SerializedName("photo") var photo : PhotoInfo?,
    @SerializedName("stat") var stat : String,
    @SerializedName("message") var message: String?
)

data class PhotoInfo (
    @SerializedName("id") var id : String,
    @SerializedName("secret") var secret : String,
    @SerializedName("server") var server : String,
    @SerializedName("farm") var farm : Int,
    @SerializedName("dateuploaded") var dateuploaded : String,
    @SerializedName("isfavorite") var isfavorite : Int,
    @SerializedName("license") var license : String,
    @SerializedName("safety_level") var safetyLevel : String,
    @SerializedName("rotation") var rotation : Int,
    @SerializedName("owner") var owner : Owner,
    @SerializedName("title") var title : StringContent,
    @SerializedName("description") var description : StringContent,
    @SerializedName("visibility") var visibility : Visibility,
    @SerializedName("dates") var dates : Dates,
    @SerializedName("views") var views : String,
    @SerializedName("editability") var editability : Canability,
    @SerializedName("publiceditability") var publiceditability : Canability,
    @SerializedName("usage") var usage : Usage,
    @SerializedName("comments") var comments : StringContent,
    @SerializedName("notes") var notes : Notes,
    @SerializedName("people") var people : People,
    @SerializedName("tags") var tags : Tags,
    @SerializedName("location") var location : Location,
    @SerializedName("geoperms") var geoperms : Geoperms,
    @SerializedName("urls") var urls : Urls,
    @SerializedName("media") var media : String
)

data class Owner (
    @SerializedName("nsid") var nsid : String,
    @SerializedName("username") var username : String,
    @SerializedName("realname") var realname : String,
    @SerializedName("location") var location : String,
    @SerializedName("iconserver") var iconserver : String,
    @SerializedName("iconfarm") var iconfarm : Int,
    @SerializedName("path_alias") var pathAlias : String
)

data class StringContent (
    @SerializedName("_content") var content : String
)

data class Visibility (
    @SerializedName("ispublic") var ispublic : Int,
    @SerializedName("isfriend") var isfriend : Int,
    @SerializedName("isfamily") var isfamily : Int
)

data class Dates (
    @SerializedName("posted") var posted : String,
    @SerializedName("taken") var taken : String,
    @SerializedName("takengranularity") var takengranularity : String,
    @SerializedName("takenunknown") var takenunknown : String,
    @SerializedName("lastupdate") var lastupdate : String
)

data class Canability (
    @SerializedName("cancomment") var cancomment : Int,
    @SerializedName("canaddmeta") var canaddmeta : Int
)

data class Usage (
    @SerializedName("candownload") var candownload : Int,
    @SerializedName("canblog") var canblog : Int,
    @SerializedName("canprint") var canprint : Int,
    @SerializedName("canshare") var canshare : Int
)

data class Notes (
    @SerializedName("note") var note : List<String>
)

data class People (
    @SerializedName("haspeople") var haspeople : Int
)

data class Tag (
    @SerializedName("id") var id : String,
    @SerializedName("author") var author : String,
    @SerializedName("authorname") var authorname : String,
    @SerializedName("raw") var raw : String,
    @SerializedName("_content") var content : String,
    //@SerializedName("machine_tag") var machineTag : Int or Boolean
)

data class Tags (
    @SerializedName("tag") var tag : List<Tag>
)

data class Location (
    @SerializedName("latitude") var latitude : String,
    @SerializedName("longitude") var longitude : String,
    @SerializedName("accuracy") var accuracy : String,
    @SerializedName("context") var context : String,
    @SerializedName("locality") var locality : StringContent,
    @SerializedName("county") var county : StringContent,
    @SerializedName("region") var region : StringContent,
    @SerializedName("country") var country : StringContent,
    @SerializedName("neighbourhood") var neighbourhood : StringContent
)

data class Geoperms (
    @SerializedName("ispublic") var ispublic : Int,
    @SerializedName("iscontact") var iscontact : Int,
    @SerializedName("isfriend") var isfriend : Int,
    @SerializedName("isfamily") var isfamily : Int
)

data class Url (
    @SerializedName("type") var type : String,
    @SerializedName("_content") var content : String
)

data class Urls (
    @SerializedName("url") var url : List<Url>
)

@Entity(
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