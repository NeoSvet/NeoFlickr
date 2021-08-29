package ru.neosvet.flickr.photo

import android.graphics.Bitmap
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.neosvet.flickr.entities.PhotoInfo

@AddToEndSingle
interface PhotoView : MvpView {
    fun setImage(bitmap: Bitmap)
    fun updateInfo()
    fun showError(t: Throwable)
}