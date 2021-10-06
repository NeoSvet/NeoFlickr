package ru.neosvet.flickr.loader

import android.content.Context
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import java.io.File

object PRDnlrLoader : Loader {
    private lateinit var folder: File
    private val tasks = HashMap<String, Int>()

    fun init(context: Context) {
        PRDownloader.initialize(context)
        folder = context.filesDir
    }

    override fun load(url: String, path: String, receiver: FileReceiver) {
        tasks[url]?.let {
            return
        }
        val file = File(path)
        val id = PRDownloader.download(url, folder.toString(), file.name)
            .build()
            .setOnProgressListener {
                it?.let {
                    receiver.onLoadProgress(url, it.currentBytes)
                }
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    tasks.remove(url)
                    val f = File(folder.toString() + "/" + file.name)
                    receiver.onFileLoaded(url, f, file)
                }

                override fun onError(error: Error?) {
                    tasks.remove(url)
                    error?.let {
                        receiver.onFileFailed(url, it.connectionException)
                    }
                }
            })
        tasks.put(url, id)
    }
}
