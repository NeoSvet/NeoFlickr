package ru.neosvet.flickr.screens

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.neosvet.flickr.entities.PhotoItem
import ru.neosvet.flickr.views.PhotoFragment

object PhotoScreen {
    fun create(photo: PhotoItem) = FragmentScreen { PhotoFragment.newInstance(photo) }
}