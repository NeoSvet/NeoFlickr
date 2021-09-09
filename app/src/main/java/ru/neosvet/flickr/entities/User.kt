package ru.neosvet.flickr.entities

import com.google.gson.annotations.SerializedName

data class UserResponse (
    @SerializedName("user") val user : User?,
    @SerializedName("stat") val stat : String,
    @SerializedName("message") val message: String?
)

data class User (
    @SerializedName("id") val id : String,
    @SerializedName("nsid") val nsid : String,
    @SerializedName("username") val username : StringContent
)
