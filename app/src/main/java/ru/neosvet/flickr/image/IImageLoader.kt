package ru.neosvet.flickr.image

import com.squareup.picasso.Target

interface IImageLoader {
    fun loadSmall(url: String, target: Target)
    fun loadUrl(url: String, target: Target)
}