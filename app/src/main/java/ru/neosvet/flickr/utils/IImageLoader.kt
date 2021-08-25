package ru.neosvet.flickr.utils

import android.widget.ImageView

interface IImageLoader {
    fun load(url: String, container: ImageView)
}