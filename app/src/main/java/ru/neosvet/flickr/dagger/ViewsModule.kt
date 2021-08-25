package ru.neosvet.flickr.dagger

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.neosvet.flickr.views.GalleryFragment
import ru.neosvet.flickr.views.MainActivity

@Module
interface ViewsModule {

    @ContributesAndroidInjector
    fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    fun bindGalleryFragment(): GalleryFragment
}