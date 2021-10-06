package ru.neosvet.flickr.image

interface IImageSource {
    fun getInnerImage(url: String, receiver: ImageReceiver)
    fun getOuterImage(url: String, receiver: ImageReceiver)
}