package ru.neosvet.flickr.image

import android.graphics.Bitmap
import ru.neosvet.flickr.entities.ImageItem

interface IImageSource {
    fun getInnerImage(url: String, receiver: ImageReceiver)
    fun getOuterImage(url: String, receiver: ImageReceiver)
    fun save(from: Bitmap, to: ImageItem)
    fun cancelLoad(url: String)
}