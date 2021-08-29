package ru.neosvet.flickr.entities

import com.google.gson.annotations.SerializedName

data class SizesResponse (
    @SerializedName("sizes") var sizes : Sizes?,
    @SerializedName("stat") var stat : String,
    @SerializedName("message") var message: String?
)

data class Sizes (
    @SerializedName("canblog") var canblog : Int,
    @SerializedName("canprint") var canprint : Int,
    @SerializedName("candownload") var candownload : Int,
    @SerializedName("size") var size : List<Size>
)

data class Size (
    @SerializedName("label") var label : String,
    @SerializedName("width") var width : Int,
    @SerializedName("height") var height : Int,
    @SerializedName("source") var source : String,
    @SerializedName("url") var url : String,
    @SerializedName("media") var media : String
)
