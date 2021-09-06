package ru.neosvet.flickr.settings

import moxy.MvpView
import moxy.viewstate.strategy.alias.Skip

@Skip
interface SettingsView : MvpView {
    fun setUser(user_name: String)
    fun setGallery(gallery_url: String)
    fun showError(field: SettingsField, t: Throwable)
    fun setGalleryType(type: GalleryType)
}

enum class SettingsField {
    User, Gallery, Other
}