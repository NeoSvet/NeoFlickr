package ru.neosvet.flickr.dagger

import dagger.Binds
import dagger.Module
import dagger.Reusable
import ru.neosvet.flickr.utils.ISettings
import ru.neosvet.flickr.utils.SettingsUtils

@Module
interface UtilsModule {
    @Reusable
    @Binds
    fun bindSettings(settings: SettingsUtils): ISettings
}