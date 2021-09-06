package ru.neosvet.flickr.gallery

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip

@AddToEndSingle
interface GalleryView : MvpView {
    fun init()
    @Skip
    fun showLoading()
    fun updateGallery()
    fun updatePages(page: Int, pages: Int)
    @Skip
    fun showError(t: Throwable)
}