package ru.neosvet.flickr.gallery

import ru.neosvet.flickr.entities.PhotoItem

interface IGalleryItemView {
    var pos: Int
    fun setPhoto(item: PhotoItem)
}