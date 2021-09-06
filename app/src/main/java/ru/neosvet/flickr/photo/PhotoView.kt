package ru.neosvet.flickr.photo

import android.graphics.Bitmap
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface PhotoView : MvpView {
    fun setImage(bitmap: Bitmap)
    fun setNoPhoto()
    fun updateInfo()
    fun showLoading()
    fun setLoadProgress(stat: String)
    fun showError(t: Throwable)
}