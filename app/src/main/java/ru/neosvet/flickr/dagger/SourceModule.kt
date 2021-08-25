package ru.neosvet.flickr.dagger

import dagger.Binds
import dagger.Module
import ru.neosvet.flickr.gallery.GallerySource
import ru.neosvet.flickr.gallery.IGallerySource
import javax.inject.Singleton

@Module
interface SourceModule {
    @Singleton
    @Binds
    fun bindGallerySource(source: GallerySource): IGallerySource
}