package ru.neosvet.flickr.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import ru.neosvet.flickr.entities.ImageItem
import ru.neosvet.flickr.loader.FileReceiver
import ru.neosvet.flickr.loader.Loader
import ru.neosvet.flickr.scheduler.Schedulers
import ru.neosvet.flickr.storage.FlickrStorage
import java.io.File
import java.io.FileNotFoundException
import java.util.*

class ImageSource(
    private val context: Context,
    private val schedulers: Schedulers,
    private val loader: Loader,
    private val storage: FlickrStorage
) : IImageSource, FileReceiver {

    companion object {
        fun getInnerPath(context: Context) = context.filesDir.toString() + "/images/"
    }

    private val outerPath: String by lazy {
        Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            .getAbsolutePath() + "/NeoFlickr/"
    }
    private val receivers = HashMap<String, ImageReceiver>()

    override fun getInnerImage(url: String, receiver: ImageReceiver) {
        receivers.put(url, receiver)
        getImage(url, getInnerPath(context))
    }

    override fun getOuterImage(url: String, receiver: ImageReceiver) {
        receivers.put(url, receiver)
        if (url.contains(".jpg"))
            getImage(url, outerPath)
        else
            getVideo(url)
    }

    private fun getImage(url: String, path: String) {
        receivers[url]?.let { receiver ->
            storage.imageDao.get(url)
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .map(this::checkFile)
                .map(this::openBitmap)
                .subscribe({
                    receiver.onImageLoaded(url, it)
                }, {
                    startLoad(url, path)
                })
        }
    }

    private fun getVideo(url: String) {
        receivers[url]?.let { receiver ->
            storage.imageDao.get(url)
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .map(this::checkFile)
                .map(this::openVideo)
                .subscribe(
                    receiver::onVideoLoaded,
                ) {
                    startLoad(url, outerPath)
                }
        }
    }

    private fun checkFile(item: ImageItem): ImageItem {
        val f = File(item.path)
        if (!f.exists())
            throw FileNotFoundException()
        return item
    }

    private fun openVideo(item: ImageItem) = Uri.parse(item.uri)

    private fun openBitmap(image: ImageItem): Bitmap {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        return BitmapFactory.decodeFile(image.path, options)
    }

    private fun startLoad(url: String, folder: String) {
        val f = File(folder)
        if (f.exists().not())
            f.mkdir()

        val id = UUID.randomUUID().toString()
        val p = if (url.contains(".jpg"))
            "$folder$id.jpg"
        else
            "$folder$id.mp4"

        loader.load(url, p, this)
    }

    private fun saveImage(url: String, file: File) {
        if (file.path.contains(outerPath)) {
            MediaScannerConnection.scanFile(
                context, arrayOf(file.toString()), null
            ) { path, uri -> }
        }

        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        val bitmap = BitmapFactory.decodeFile(file.toString(), options)
        receivers[url]?.onImageLoaded(url, bitmap)
        insertImageItem(
            ImageItem(url, file.toString(), null)
        )
    }

    private fun saveVideo(url: String, file: File) {
        val live = MutableLiveData<Uri>()
        live.observeForever {
            receivers[url]?.onVideoLoaded(it)
        }

        MediaScannerConnection.scanFile(
            context, arrayOf(file.toString()), null
        ) { path, uri ->
            live.postValue(uri)
            insertImageItem(
                ImageItem(url, file.toString(), uri.toString())
            )
        }
    }

    private fun insertImageItem(item: ImageItem) {
        storage.imageDao.insert(item)
            .observeOn(schedulers.main())
            .subscribeOn(schedulers.background())
            .subscribe()
    }

    override fun onFileLoaded(url: String, tempFile: File, file: File) {
        val folder = file.parentFile
        if (!folder.exists())
            folder.mkdir()
        tempFile.copyTo(file)
        tempFile.delete()
        if (url.contains(".jpg"))
            saveImage(url, file)
        else
            saveVideo(url, file)
    }

    override fun onFileFailed(url: String, t: Throwable) {
        receivers[url]?.onFailed(t)
    }

    override fun onLoadProgress(url: String, bytes: Long) {
        val kb = bytes / 1024f
        receivers[url]?.onLoadProgress(String.format("%.1f KB", kb))
    }
}