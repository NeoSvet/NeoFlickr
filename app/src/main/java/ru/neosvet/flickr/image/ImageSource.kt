package ru.neosvet.flickr.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Environment
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.reactivex.rxjava3.core.Completable
import ru.neosvet.flickr.entities.ImageItem
import ru.neosvet.flickr.scheduler.Schedulers
import ru.neosvet.flickr.storage.FlickrStorage
import java.io.File
import java.io.FileOutputStream
import java.util.*

class ImageSource(
    private val context: Context,
    private val schedulers: Schedulers,
    private val loader: IImageLoader,
    private val storage: FlickrStorage
) : IImageSource {

    override fun getInnerImage(url: String, receiver: ImageReceiver) {
        val path = context.filesDir.toString() + "/images/"

        getImage(url, path, receiver)
    }

    override fun getOuterImage(url: String, receiver: ImageReceiver) {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            .getAbsolutePath() + "/NeoFlickr/"

        getImage(url, path, receiver)
    }

    private fun getImage(url: String, path: String, receiver: ImageReceiver) {
        val f = File(path)
        if (!f.exists())
            f.mkdir()

        storage.imageDao.get(url)
            .observeOn(schedulers.main())
            .subscribeOn(schedulers.background())
            .map(this::openBitmap)
            .subscribe(
                receiver::onImageLoaded
            ) {
                startLoad(url, path, receiver)
            }
    }

    private fun openBitmap(image: ImageItem): Bitmap {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        return BitmapFactory.decodeFile(image.path, options)
    }

    private fun startLoad(url: String, path: String, receiver: ImageReceiver) {
        loader.load(url, object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                bitmap?.let {
                    receiver.onImageLoaded(it)

                    val id = UUID.randomUUID().toString()
                    val image = ImageItem(
                        url = url,
                        path = "$path$id.jpg"
                    )
                    saveImage(it, image)
                        .observeOn(schedulers.main())
                        .subscribeOn(schedulers.background())
                        .subscribe()
                }
            }

            override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                e?.let {
                    receiver.onImageFailed(it)
                }
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            }

        })
    }

    private fun saveImage(bitmap: Bitmap, image: ImageItem) = Completable.fromCallable {
        FileOutputStream(File(image.path)).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos)
        }
        storage.imageDao.insert(image)
            .observeOn(schedulers.main())
            .subscribeOn(schedulers.background())
            .subscribe()
    }

}