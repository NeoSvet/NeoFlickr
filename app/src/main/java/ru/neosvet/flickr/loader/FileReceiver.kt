package ru.neosvet.flickr.loader

import java.io.File

interface FileReceiver {
    fun onFileLoaded(url: String, tempFile:File, file: File)
    fun onFileFailed(url: String, t: Throwable)
    fun onLoadProgress(url: String, bytes: Long)
}