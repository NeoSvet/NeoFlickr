package ru.neosvet.flickr.dagger

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.neosvet.flickr.views.GalleryFragment
import ru.neosvet.flickr.views.MainActivity
import ru.neosvet.flickr.views.PhotoFragment
import ru.neosvet.flickr.views.SettingsFragment

@Module
interface ViewsModule {

    @ContributesAndroidInjector
    fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    fun bindGalleryFragment(): GalleryFragment

    @ContributesAndroidInjector
    fun bindPhotoFragment(): PhotoFragment

    @ContributesAndroidInjector
    fun bindSettingsFragment(): SettingsFragment

}