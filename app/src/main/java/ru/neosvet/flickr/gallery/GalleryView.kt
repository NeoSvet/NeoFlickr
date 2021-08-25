package ru.neosvet.flickr.gallery

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface GalleryView : MvpView {
    fun init()
    fun updateList()
    fun showError(t: Throwable)
}