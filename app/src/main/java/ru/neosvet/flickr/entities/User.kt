package ru.neosvet.flickr.entities

import com.google.gson.annotations.SerializedName

data class UserResponse (
    @SerializedName("user") var user : User?,
    @SerializedName("stat") var stat : String,
    @SerializedName("message") var message: String?
)

data class User (
    @SerializedName("id") var id : String,
    @SerializedName("nsid") var nsid : String,
    @SerializedName("username") var username : StringContent
)
