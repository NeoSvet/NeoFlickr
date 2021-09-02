package ru.neosvet.flickr.screens

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.neosvet.flickr.views.PhotoFragment

object PhotoScreen {
    fun create(photoId: String) = FragmentScreen { PhotoFragment.newInstance(photoId) }
}