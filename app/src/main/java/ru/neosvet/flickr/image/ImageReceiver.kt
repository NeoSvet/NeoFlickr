package ru.neosvet.flickr.image

import android.graphics.Bitmap
import android.net.Uri

interface ImageReceiver {
    fun onImageLoaded(url: String, bitmap: Bitmap)
    fun onVideoLoaded(uri: Uri)
    fun onFailed(t: Throwable)
    fun onLoadProgress(stat: String)
}