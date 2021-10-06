package ru.neosvet.flickr.loader

interface Loader {
    fun load(url: String, path: String, receiver: FileReceiver)
}