package ru.neosvet.flickr.image

import android.graphics.Bitmap
import ru.neosvet.flickr.entities.ImageItem
import java.io.File

interface ImageReceiver {
    var saveAs: ImageItem?
    fun startLoading()
    fun onImageLoaded(bitmap: Bitmap)
    fun onVideoLoaded(file: File)
    fun onImageFailed(t: Throwable)
    fun onLoadProgress(bytes: Long)
}