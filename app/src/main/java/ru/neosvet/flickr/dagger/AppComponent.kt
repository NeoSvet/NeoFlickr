package ru.neosvet.flickr.dagger

import android.content.Context
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import ru.neosvet.android4.mvp.model.network.INetworkStatus
import ru.neosvet.flickr.NeoFlickr
import ru.neosvet.flickr.scheduler.Schedulers
import ru.neosvet.flickr.storage.FlickrStorage
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidInjectionModule::class,
        ViewsModule::class,
        ApiModule::class,
        SourceModule::class,
        UtilsModule::class]
)
interface AppComponent : AndroidInjector<NeoFlickr> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun withContext(context: Context): Builder

        @BindsInstance
        fun withRouter(router: Router): Builder

        @BindsInstance
        fun withNavigatorHolder(navigatorHolder: NavigatorHolder): Builder

        @BindsInstance
        fun withSchedulers(schedulers: Schedulers): Builder

        @BindsInstance
        fun withStorage(storage: FlickrStorage): Builder

        @BindsInstance
        fun withNetworkStatus(networkStatus: INetworkStatus): Builder

        fun build(): AppComponent

    }

}