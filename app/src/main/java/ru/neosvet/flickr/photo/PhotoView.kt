package ru.neosvet.flickr.photo

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface PhotoView : MvpView {
    fun setImage(bitmap: Bitmap)
    fun setNoPhoto()
    fun updateInfo()
    fun showError(t: Throwable)
}