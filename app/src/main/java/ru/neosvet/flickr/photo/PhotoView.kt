package ru.neosvet.flickr.photo

import android.graphics.Bitmap
import android.net.Uri
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import moxy.viewstate.strategy.alias.Skip
import java.io.File

@AddToEndSingle
interface PhotoView : MvpView {
    fun setImage(bitmap: Bitmap)
    fun setVideo(uri: Uri)
    @Skip
    fun setNoPhoto()
    fun updateInfo()
    @OneExecution
    fun showLoading()
    @OneExecution
    fun setLoadProgress(stat: String)
    @Skip
    fun showError(t: Throwable)
}