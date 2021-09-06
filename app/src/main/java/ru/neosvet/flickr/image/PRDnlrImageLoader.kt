package ru.neosvet.flickr.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import java.io.File

object PRDnlrImageLoader : IImageLoader {
    private lateinit var folder: File
    private val tasks = HashMap<String, Int>()

    fun init(context: Context) {
        PRDownloader.initialize(context)
        folder = context.filesDir
    }

    override fun load(url: String, receiver: ImageReceiver) {
        tasks[url]?.let {
            return
        }
        receiver.saveAs?.let {
            if (!folder.exists())
                folder.mkdir()
            val file = File(it.path)
            val id = PRDownloader.download(url, folder.toString(), file.name)
                .build()
                .setOnProgressListener {
                    it?.let {
                        receiver.onLoadProgress(it.currentBytes)
                    }
                }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        onFinish(url, file, receiver)
                    }

                    override fun onError(error: Error?) {
                        error?.let {
                            receiver.onImageFailed(it.connectionException)
                        }
                    }
                })
            tasks.put(url, id)
        }
    }

    private fun onFinish(url: String, file: File, receiver: ImageReceiver) {
        tasks.remove(url)
        val f = File(folder.toString() + "/" + file.name)
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        val bitmap = BitmapFactory.decodeFile(f.toString(), options)
        receiver.onImageLoaded(bitmap)
        f.delete()
    }

    override fun cancel(url: String) {
        tasks[url]?.let {
            tasks.remove(url)
            PRDownloader.cancel(it)
        }
    }
}
