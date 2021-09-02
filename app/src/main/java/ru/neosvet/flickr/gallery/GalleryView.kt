package ru.neosvet.flickr.gallery

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface GalleryView : MvpView {
    fun init()
    fun showLoading()
    fun updateGallery()
    fun updatePages(page: Int, pages: Int)
    fun showError(t: Throwable)
}