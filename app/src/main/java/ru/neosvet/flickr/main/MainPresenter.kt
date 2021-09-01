package ru.neosvet.flickr.main

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.neosvet.flickr.screens.GalleryScreen

class MainPresenter(
    private val router: Router
) : MvpPresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        router.replaceScreen(GalleryScreen.create())
    }

    fun back() {
        router.exit()
    }
}