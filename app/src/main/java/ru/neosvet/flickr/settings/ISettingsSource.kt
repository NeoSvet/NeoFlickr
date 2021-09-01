package ru.neosvet.flickr.settings

import io.reactivex.rxjava3.core.Single
import ru.neosvet.flickr.entities.GalleryResponse
import ru.neosvet.flickr.entities.UserResponse

interface ISettingsSource {
    fun findUser(user_name: String): Single<UserResponse>
    fun findGallery(gallery_url: String): Single<GalleryResponse>
}