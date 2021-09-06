package ru.neosvet.flickr.image

interface IImageLoader {
    fun load(url: String, receiver: ImageReceiver)
    fun cancel(url: String)
}