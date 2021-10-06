package ru.neosvet.flickr.main

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.neosvet.flickr.entities.PhotoItem
import ru.neosvet.flickr.screens.GalleryScreen
import ru.neosvet.flickr.screens.PhotoScreen

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