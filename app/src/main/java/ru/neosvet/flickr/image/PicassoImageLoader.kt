package ru.neosvet.flickr.image

import android.net.Uri
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

object PicassoImageLoader : IImageLoader {
    override fun load(url: String, target: Target) {
        Picasso.get()
            .load(Uri.parse(url))
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .into(target)
    }
}