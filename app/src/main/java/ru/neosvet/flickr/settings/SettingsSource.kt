package ru.neosvet.flickr.settings

import ru.neosvet.flickr.api.Client
import javax.inject.Inject

class SettingsSource @Inject constructor(
    private val api: Client
) : ISettingsSource {

    override fun findUser(user_name: String) = api.findUser(user_name)

    override fun findGallery(gallery_url: String) = api.findGallery(gallery_url)
}