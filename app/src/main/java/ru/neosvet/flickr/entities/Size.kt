package ru.neosvet.flickr.entities

import com.google.gson.annotations.SerializedName

data class SizesResponse (
    @SerializedName("sizes") val sizes : Sizes?,
    @SerializedName("stat") val stat : String,
    @SerializedName("message") val message: String?
)

data class Sizes (
    @SerializedName("canblog") val canblog : Int,
    @SerializedName("canprint") val canprint : Int,
    @SerializedName("candownload") val candownload : Int,
    @SerializedName("size") val size : List<Size>
)

data class Size (
    @SerializedName("label") val label : String,
    @SerializedName("width") val width : Int,
    @SerializedName("height") val height : Int,
    @SerializedName("source") val source : String,
    @SerializedName("url") val url : String,
    @SerializedName("media") val media : String
)
