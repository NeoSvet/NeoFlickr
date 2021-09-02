package ru.neosvet.flickr.photo

import android.graphics.Bitmap
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
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

    override fun onDestroy() {
        process?.dispose()
    }

    fun load(photoId: String) {
        process = photo.getUrlBig(photoId)
            .observeOn(schedulers.main())
            .subscribeOn(schedulers.background())
            .subscribe(
                this::loadUrl,
                viewState::showError
            )
    }

    private fun loadUrl(url: String) {
        image.getOuterImage(url, this)
    }

    fun getInfo(photoId: String) {
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
            add(Pair(titleIds.title, info.title))
            if (info.description.isNotEmpty())
                add(Pair(titleIds.description, info.description))
        }
        viewState.updateInfo()
    }

    override fun onImageLoaded(bitmap: Bitmap) {
        viewState.setImage(bitmap)
    }

    override fun onImageFailed(t: Throwable) {
        viewState.showError(t)
        viewState.setNoPhoto()
    }
}