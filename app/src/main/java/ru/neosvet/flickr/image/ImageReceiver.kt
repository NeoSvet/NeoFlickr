package ru.neosvet.flickr.image

import android.graphics.Bitmap
import ru.neosvet.flickr.entities.ImageItem

interface ImageReceiver {
    var saveAs: ImageItem?
    fun onImageLoaded(bitmap: Bitmap)
    fun onImageFailed(t: Throwable)
    fun onLoadProgress(bytes: Long)
}