package ru.neosvet.flickr.settings

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import ru.neosvet.flickr.entities.GalleryResponse
import ru.neosvet.flickr.entities.UserResponse
import ru.neosvet.flickr.scheduler.Schedulers
import ru.neosvet.flickr.screens.GalleryScreen
import java.io.File

class SettingsPresenter(
    private val source: ISettingsSource,
    private val settings: ISettings,
    private val router: Router,
    private val schedulers: Schedulers
) : MvpPresenter<SettingsView>() {

    private var process: Disposable? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadSettings()
    }

    override fun onDestroy() {
        settings.save()
        process?.dispose()
    }

    private fun loadSettings() {
        viewState.setGalleryType(settings.getGalleryType())
        viewState.setUser(settings.getUserName())
        viewState.setGallery(settings.getGalleryUrl())
    }

    fun deleteFolder(path: String) {
        try {
            val folder = File(path)
            folder.listFiles().forEach {
                it.delete()
            }
            folder.delete()
        } catch (e: Exception) {
        }
    }

    fun back(): Boolean {
        settings.save()
        router.replaceScreen(GalleryScreen.create())
        return true
    }

    fun setUser(user_name: String) {
        process = source.findUser(user_name)
            .observeOn(schedulers.main())
            .subscribeOn(schedulers.background())
            .map {
                if (it.stat.equals("fail"))
                    throw Exception(it.message)
                it
            }
            .subscribe(
                this::parseUser,
                {
                    viewState.showError(SettingsField.User, it)
                }
            )
    }

    private fun parseUser(response: UserResponse) {
        response.user?.let {
            settings.setUserId(it.id)
            settings.setUserName(it.username.content)
        }
    }

    fun setGallery(gallery_url: String) {
        process = source.findGallery(gallery_url)
            .observeOn(schedulers.main())
            .subscribeOn(schedulers.background())
            .map {
                if (it.stat.equals("fail"))
                    throw Exception(it.message)
                it
            }
            .subscribe(
                this::parseGallery,
                {
                    viewState.showError(SettingsField.Gallery, it)
                })
    }

    private fun parseGallery(response: GalleryResponse) {
        response.gallery?.let {
            settings.setGalleryId(it.id)
            settings.setGalleryUrl(it.url)
        }
    }

    fun setGalleryType(type: GalleryType) {
        viewState.setGalleryType(type)
        settings.setGalleryType(type)
    }
}