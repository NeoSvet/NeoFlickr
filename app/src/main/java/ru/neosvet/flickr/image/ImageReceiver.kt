package ru.neosvet.flickr.image

import android.graphics.Bitmap

interface ImageReceiver {
    fun onImageLoaded(bitmap: Bitmap)
    fun onImageFailed(t: Throwable)
}