package ru.neosvet.flickr.utils

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class SettingsUtils @Inject constructor(
    private val context: Context
) : ISettings {
    private val NAME = "settings"
    private val GALLERY_TYPE = "gallery_type"
    private val DEFAULT_GALLERY_TYPE = 0

    private val USER_ID = "user_id"
    private val DEFAULT_USER_ID = "98635224@N07"
    private val USER_NAME = "user_name"
    private val DEFAULT_USER_NAME = "Raine Photos"

    private val GALLERY_ID = "gallery_id"
    private val DEFAULT_GALLERY_ID = "66911286-72157651480893056"
    private val GALLERY_URL = "gallery_url"
    private val DEFAULT_GALLERY_URL =
        "https://www.flickr.com/photos/flickr/galleries/72157651480893056"

    private var isChanged = false

    private val pref: SharedPreferences by lazy {
        context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }
    private val editor: SharedPreferences.Editor by lazy {
        pref.edit()
    }

    override fun save() {
        if (isChanged) {
            editor.apply()
            isChanged = false
        }
    }

    override fun getGalleryType() = when (pref.getInt(GALLERY_TYPE, DEFAULT_GALLERY_TYPE)) {
        1 -> GalleryType.Gallery
        else -> GalleryType.Popular
    }

    override fun setGalleryType(type: GalleryType) {
        editor.putInt(GALLERY_TYPE, type.index)
        isChanged = true
    }

    override fun getUserId(): String {
        pref.getString(USER_ID, null)?.let {
            if (it.length > 3)
                return it
        }
        return DEFAULT_USER_ID
    }

    override fun setUserId(id: String) {
        editor.putString(USER_ID, id)
        isChanged = true
    }

    override fun getUserName(): String {
        pref.getString(USER_NAME, null)?.let {
            if (it.length > 3)
                return it
        }
        return DEFAULT_USER_NAME
    }

    override fun setUserName(name: String) {
        editor.putString(USER_NAME, name)
        isChanged = true
    }

    override fun getGalleryId(): String {
        pref.getString(GALLERY_ID, null)?.let {
            if (it.length > 3)
                return it
        }
        return DEFAULT_GALLERY_ID
    }

    override fun setGalleryId(id: String) {
        editor.putString(GALLERY_ID, id)
        isChanged = true
    }

    override fun getGalleryUrl(): String {
        pref.getString(GALLERY_URL, null)?.let {
            if (it.length > 3)
                return it
        }
        return DEFAULT_GALLERY_URL
    }

    override fun setGalleryUrl(url: String) {
        editor.putString(GALLERY_URL, url)
        isChanged = true
    }
}