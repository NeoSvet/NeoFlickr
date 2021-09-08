package ru.neosvet.flickr.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Environment
import io.reactivex.rxjava3.core.Completable
import ru.neosvet.flickr.entities.ImageItem
import ru.neosvet.flickr.scheduler.Schedulers
import ru.neosvet.flickr.storage.FlickrStorage
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.*

class ImageSource(
    private val context: Context,
    private val schedulers: Schedulers,
    private val loader: IImageLoader,
    private val storage: FlickrStorage
) : IImageSource {

    companion object {
        fun getInnerPath(context: Context) = context.filesDir.toString() + "/images/"
    }

    private val outerPath: String by lazy {
        Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            .getAbsolutePath() + "/NeoFlickr/"
    }

    override fun getInnerImage(url: String, receiver: ImageReceiver) {
        getImage(url, getInnerPath(context), receiver)
    }

    override fun getOuterImage(url: String, receiver: ImageReceiver) {
        getImage(url, outerPath, receiver)
    }

    private fun getImage(url: String, path: String, receiver: ImageReceiver) {
        val f = File(path)
        if (!f.exists())
            f.mkdir()

        if (url.contains(".jpg")) {
            storage.imageDao.get(url)
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .map(this::openBitmap)
                .subscribe(
                    receiver::onImageLoaded
                ) {
                    startLoad(url, path, receiver)
                }
        } else { //is video
            storage.imageDao.get(url)
                .observeOn(schedulers.main())
                .subscribeOn(schedulers.background())
                .map(this::openFile)
                .subscribe(
                    receiver::onVideoLoaded,
                ) {
                    startLoad(url, path, receiver)
                }
        }
    }

    private fun openFile(item: ImageItem) = File(item.path).also {
        if (!it.exists())
            throw FileNotFoundException()
    }

    private fun openBitmap(image: ImageItem): Bitmap {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        return BitmapFactory.decodeFile(image.path, options)
    }

    override fun save(from: Bitmap, to: ImageItem) {
        saveImage(from, to)
            .observeOn(schedulers.main())
            .subscribeOn(schedulers.background())
            .subscribe()
    }

    override fun saveItem(item: ImageItem) {
        storage.imageDao.insert(item)
            .observeOn(schedulers.main())
            .subscribeOn(schedulers.background())
            .subscribe()
    }

    override fun cancelLoad(url: String) {
        loader.cancel(url)
    }

    private fun startLoad(url: String, path: String, receiver: ImageReceiver) {
        receiver.startLoading()
        val id = UUID.randomUUID().toString()
        receiver.saveAs = ImageItem(
            url = url,
            path = "$path$id.jpg"
        )
        loader.load(url, receiver)
    }

    private fun saveImage(bitmap: Bitmap, image: ImageItem) = Completable.fromCallable {
        val file = File(image.path)
        FileOutputStream(file).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos)
        }
        if (image.path.contains(outerPath))
            MediaScannerConnection.scanFile(
                context, arrayOf(file.toString()), null
            ) { path, uri -> }

        saveItem(image)
    }

}