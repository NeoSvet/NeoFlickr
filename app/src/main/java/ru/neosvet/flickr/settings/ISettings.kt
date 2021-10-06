package ru.neosvet.flickr.settings

interface ISettings {
    fun save()
    fun getGalleryType(): GalleryType
    fun setGalleryType(type: GalleryType)
    fun getUserId(): String
    fun setUserId(id: String)
    fun getUserName(): String
    fun setUserName(name: String)
    fun getGalleryId(): String
    fun setGalleryId(id: String)
    fun getGalleryUrl(): String
    fun setGalleryUrl(url: String)
}

enum class GalleryType(val index: Int) {
    Popular(0), Gallery(1)
}