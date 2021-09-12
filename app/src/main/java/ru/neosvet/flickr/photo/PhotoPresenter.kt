package ru.neosvet.flickr.photo

import android.graphics.Bitmap
import android.net.Uri
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import ru.neosvet.flickr.abs.MyAction
import ru.neosvet.flickr.entities.InfoItem
import ru.neosvet.flickr.image.IImageSource
import ru.neosvet.flickr.image.ImageReceiver
import ru.neosvet.flickr.scheduler.Schedulers

class PhotoPresenter(
    private val titleIds: TitleIds,
    private val photo: IPhotoSource,
    private val image: IImageSource,
    private val schedulers: Schedulers
) : MvpPresenter<PhotoView>(), ImageReceiver {

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
    private var sizes: String? = null
    private var action: MyAction? = null

    override fun onDestroy() {
        process?.dispose()
    }

    fun retryLastAction() {
        action?.let {
            when (it.type) {
                MyAction.Type.PHOTO -> load(it.sArg)
                MyAction.Type.INFO -> getInfo(it.sArg)
            }
        }
    }

    fun load(photoId: String, urlMini: String? = null) {
        viewState.showLoading()
        action = MyAction(
            type = MyAction.Type.PHOTO,
            sArg = photoId,
            iArg = 0
        )
        urlMini?.let {
            image.getInnerImage(it, this)
        }
        process = photo.getUrlBig(photoId)
            .observeOn(schedulers.main())
            .subscribeOn(schedulers.background())
            .subscribe(
                this::loadUrl,
                viewState::showError
            )
    }

    private fun loadUrl(url: String) {
        viewState.showLoading()
        if (url.isNotEmpty())
            image.getOuterImage(url, this)
    }

    fun getInfo(photoId: String) {
        viewState.showLoading()
        action = MyAction(
            type = MyAction.Type.INFO,
            sArg = photoId,
            iArg = 0
        )
        process = photo.getInfo(photoId)
            .observeOn(schedulers.main())
            .subscribeOn(schedulers.background())
            .subscribe(
                this::parseInfo,
                viewState::showError
            )
    }

    private fun parseInfo(info: InfoItem) {
        infoListPresenter.list.run {
            clear()
            add(Pair(titleIds.owner, info.owner))
            add(Pair(titleIds.date, info.date))
            sizes?.let {
                add(Pair(titleIds.sizes, it))
            }
            add(Pair(titleIds.title, info.title))
            if (info.description.isNotEmpty())
                add(Pair(titleIds.description, info.description))
        }
        viewState.updateInfo()
    }

    override fun onImageLoaded(url: String, bitmap: Bitmap) {
        sizes = "${bitmap.width} x ${bitmap.height}"
        viewState.setImage(bitmap)
    }

    override fun onVideoLoaded(uri: Uri) {
        sizes = null
        viewState.setVideo(uri)
    }

    override fun onFailed(t: Throwable) {
        viewState.showError(t)
        viewState.setNoPhoto()
    }

    override fun onLoadProgress(stat: String) {
        viewState.setLoadProgress(stat)
    }
}