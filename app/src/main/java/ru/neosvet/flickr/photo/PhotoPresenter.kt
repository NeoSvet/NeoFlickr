package ru.neosvet.flickr.photo

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.github.terrakok.cicerone.Router
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import ru.neosvet.flickr.entities.InfoResponse
import ru.neosvet.flickr.entities.Owner
import ru.neosvet.flickr.entities.Size
import ru.neosvet.flickr.entities.SizesResponse
import ru.neosvet.flickr.image.IImageLoader
import ru.neosvet.flickr.scheduler.Schedulers
import java.util.*

class PhotoPresenter(
    private val imageLoader: IImageLoader,
    private val titleIds: TitleIds,
    private val source: IPhotoSource,
    private val schedulers: Schedulers
) : MvpPresenter<PhotoView>(), Target {

    class InfoListPresenter() : IInfoListPresenter {
        val list = mutableListOf<Pair<Int, String>>()
        override var itemClickListener: ((IInfoItemView) -> Unit)? = null

        override fun bindView(view: IInfoItemView) {
            val item = list[view.pos]
            view.setText(item.first, item.second)
        }

        override fun getCount() = list.size
    }

    val infoListPresenter = InfoListPresenter()
    private var process: Disposable? = null

    override fun onDestroy() {
        process?.dispose()
    }

    fun load(photo_id: String) {
        process = source.getSizes(photo_id)
            .observeOn(schedulers.main())
            .subscribeOn(schedulers.background()).map {
                if (it.stat.equals("fail"))
                    throw Exception(it.message)
                it
            }
            .subscribe(
                this::parseSizes,
                viewState::showError
            )
    }

    private fun parseSizes(response: SizesResponse) {
        response.sizes?.size?.let {
            val url = findUrlBiggest(it)
            imageLoader.load(url, this)
        }
    }

    fun getInfo(photo_id: String) {
        process = source.getInfo(photo_id)
            .observeOn(schedulers.main())
            .subscribeOn(schedulers.background())
            .map {
                if (it.stat.equals("fail"))
                    throw Exception(it.message)
                it
            }
            .subscribe(
                this::parseInfo,
                viewState::showError
            )
    }

    private fun parseInfo(response: InfoResponse) {
        response.photo?.let {
            infoListPresenter.list.run {
                clear()
                add(Pair(titleIds.owner, getOwnerName(it.owner)))
                add(Pair(titleIds.date, dateToString(it.dates.posted)))
                add(Pair(titleIds.title, it.title.content))
                if (it.description.content.isNotEmpty())
                    add(Pair(titleIds.description, it.description.content))
            }
            viewState.updateInfo()
        }
    }

    private fun getOwnerName(owner: Owner) = if (owner.realname.isNotEmpty())
        "${owner.username} (${owner.realname})"
    else
        owner.username


    private fun dateToString(value: String): String {
        val date = Date(value.toLong() * 1000)
        return date.toLocaleString()
    }

    private fun findUrlBiggest(list: List<Size>): String {
        var max = list[0]
        list.forEach {
            if (max.width < it.width)
                max = it
        }
        return max.source
    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        bitmap?.let {
            viewState.setImage(it)
        }
    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
        e?.let {
            viewState.showError(it)
        }
        viewState.setNoPhoto()
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
    }
}