package ru.neosvet.flickr

import android.graphics.Bitmap
import ru.neosvet.flickr.image.IImageSource
import ru.neosvet.flickr.image.ImageReceiver

class TestImageSource : IImageSource {
    companion object {
        var image: Bitmap? = null
            private set
    }

    override fun getInnerImage(url: String, receiver: ImageReceiver) {
        if (image == null) {
            image = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)
        }
        image?.let {
            receiver.onImageLoaded(url, it)
        }
    }

    override fun getOuterImage(url: String, receiver: ImageReceiver) {
        getInnerImage(url, receiver)
    }
}