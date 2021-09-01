package ru.neosvet.flickr

import com.github.terrakok.cicerone.Cicerone
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import ru.neosvet.flickr.dagger.DaggerAppComponent
import ru.neosvet.flickr.scheduler.DefaultSchedulers

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
            }
            .build()

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler {
            it.printStackTrace()
        }
    }
}