package ru.neosvet.flickr.photo

import android.graphics.Bitmap
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import moxy.viewstate.strategy.alias.Skip

@AddToEndSingle
interface PhotoView : MvpView {
    fun setImage(bitmap: Bitmap)
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