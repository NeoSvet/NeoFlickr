package ru.neosvet.flickr.screens

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.neosvet.flickr.views.GalleryFragment

object GalleryScreen {
    fun create() = FragmentScreen { GalleryFragment() }
}