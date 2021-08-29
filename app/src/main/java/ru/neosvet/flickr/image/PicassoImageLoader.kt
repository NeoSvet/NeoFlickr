package ru.neosvet.flickr.image

import android.net.Uri
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

object PicassoImageLoader : IImageLoader {
    override fun loadSmall(url: String, target: Target) {
        loadUrl(url + "_w.jpg", target)
    }

    override fun loadUrl(url: String, target: Target) {
        Picasso.get().load(Uri.parse(url)).into(target)
    }
}