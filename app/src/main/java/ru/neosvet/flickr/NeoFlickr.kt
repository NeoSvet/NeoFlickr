package ru.neosvet.flickr

import com.github.terrakok.cicerone.Cicerone
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import ru.neosvet.android4.ui.network.MyNetworkStatus
import ru.neosvet.flickr.dagger.DaggerAppComponent
import ru.neosvet.flickr.image.PRDnlrImageLoader
import ru.neosvet.flickr.scheduler.DefaultSchedulers
import ru.neosvet.flickr.storage.FlickrStorage

class NeoFlickr : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<NeoFlickr> =
        DaggerAppComponent
            .builder()
            .withContext(applicationContext)
            .apply {
                val cicerone = Cicerone.create()
                withNavigatorHolder(cicerone.getNavigatorHolder())
                withRouter(cicerone.router)

                withSchedulers(DefaultSchedulers())
                withStorage(FlickrStorage.get(applicationContext))
                withNetworkStatus(MyNetworkStatus(applicationContext))
            }
            .build()

    override fun onCreate() {
        super.onCreate()
        PRDnlrImageLoader.init(applicationContext)
        RxJavaPlugins.setErrorHandler {
            it.printStackTrace()
        }
    }
}