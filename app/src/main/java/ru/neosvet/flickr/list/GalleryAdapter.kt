package ru.neosvet.flickr.list

import android.graphics.Bitmap
import android.graphics.Point
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.neosvet.flickr.R
import ru.neosvet.flickr.databinding.ItemGalleryBinding
import ru.neosvet.flickr.entities.ImageItem
import ru.neosvet.flickr.entities.PhotoItem
import ru.neosvet.flickr.gallery.IGalleryItemView
import ru.neosvet.flickr.gallery.IGalleryListPresenter
import ru.neosvet.flickr.image.IImageSource
import ru.neosvet.flickr.image.ImageReceiver
import ru.neosvet.flickr.views.Orientation
import java.io.File

class GalleryAdapter(
    private val presenter: IGalleryListPresenter,
    private val source: IImageSource,
    private val orientation: Orientation
) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    private var sizeItem: Point? = null
    private fun getSize(vb: ItemGalleryBinding): Point {
        sizeItem?.let {
            return it
        }
        val s = Point(vb.root.width, vb.root.height)
        sizeItem = s
        return s
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemGalleryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener { presenter.itemClickListener?.invoke(this) }
        }

    override fun getItemCount() = presenter.getCount()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        presenter.bindView(holder.apply { pos = position })

    inner class ViewHolder(private val vb: ItemGalleryBinding) :
        RecyclerView.ViewHolder(vb.root),
        IGalleryItemView, ImageReceiver {
        override var pos = -1
        override var saveAs: ImageItem? = null

        private var url: String? = null

        override fun setPhoto(item: PhotoItem) = with(vb) {
            url?.let {
                source.cancelLoad(it)
            }
            tvTitle.text = item.title
            url = item.urlMini
            source.getInnerImage(item.urlMini, this@ViewHolder)
        }

        override fun startLoading() {
            vb.ivPhoto.setImageResource(R.drawable.load_photo)
        }

        override fun onImageLoaded(bitmap: Bitmap) {
            vb.ivPhoto.setImageBitmap(bitmap)
            saveAs?.let {
                source.save(bitmap, it)
            }

            updateSize(bitmap.width, bitmap.height)
        }

        private fun updateSize(width: Int, height: Int) {
            val r = if (orientation == Orientation.LANDSCAPE)
                getSize(vb).y.toFloat() / height
            else
                getSize(vb).x.toFloat() / width

            val h = (height * r).toInt()
            val w = (width * r).toInt()

            with(vb.root) {
                if (layoutParams.height != h || layoutParams.width != w) {
                    layoutParams = layoutParams.apply {
                        this.height = h
                        this.width = w
                    }
                    this@GalleryAdapter.notifyItemChanged(pos)
                }
            }
        }

        override fun onImageFailed(t: Throwable) {
            vb.ivPhoto.setImageResource(R.drawable.no_photo)
        }

        override fun onLoadProgress(bytes: Long) {
        }
    }
}