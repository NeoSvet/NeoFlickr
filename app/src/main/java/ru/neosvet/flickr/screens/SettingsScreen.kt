package ru.neosvet.flickr.screens

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.neosvet.flickr.views.SettingsFragment

object SettingsScreen {
    fun create() = FragmentScreen { SettingsFragment() }
}