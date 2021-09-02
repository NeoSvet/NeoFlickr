package ru.neosvet.flickr.dagger

import dagger.Binds
import dagger.Module
import dagger.Reusable
import ru.neosvet.flickr.settings.ISettings
import ru.neosvet.flickr.settings.SettingsUtils

@Module
interface UtilsModule {
    @Reusable
    @Binds
    fun bindSettings(settings: SettingsUtils): ISettings
}