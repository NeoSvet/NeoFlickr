package ru.neosvet.flickr.list

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import ru.neosvet.flickr.R
import ru.neosvet.flickr.databinding.ItemGalleryBinding
import ru.neosvet.flickr.entities.PhotoItem
import ru.neosvet.flickr.gallery.IGalleryItemView
import ru.neosvet.flickr.gallery.IGalleryListPresenter
import ru.neosvet.flickr.image.IImageLoader
import ru.neosvet.flickr.image.IImageSource
import ru.neosvet.flickr.image.ImageReceiver

class GalleryAdapter(
    private val presenter: IGalleryListPresenter,
    private val source: IImageSource
) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

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

    inner class ViewHolder(private val vb: ItemGalleryBinding) : RecyclerView.ViewHolder(vb.root),
        IGalleryItemView, ImageReceiver {
        override var pos = -1

        override fun setPhoto(item: PhotoItem) = with(vb) {
            tvTitle.text = item.title
            ivPhoto.setImageResource(R.drawable.load_photo)
            source.getInnerImage(item.urlMini, this@ViewHolder)
        }

        override fun onImageLoaded(bitmap: Bitmap) {
            vb.ivPhoto.setImageBitmap(bitmap)
        }

        override fun onImageFailed(t: Throwable) {
            vb.ivPhoto.setImageResource(R.drawable.no_photo)
        }
    }
}