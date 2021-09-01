package ru.neosvet.flickr.image

import com.squareup.picasso.Target

interface IImageLoader {
    fun load(url: String, target: Target)
}