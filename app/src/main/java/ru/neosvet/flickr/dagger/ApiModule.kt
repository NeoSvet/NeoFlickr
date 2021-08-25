package ru.neosvet.flickr.dagger

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.neosvet.flickr.api.Client
import ru.neosvet.flickr.api.MyInterceptor
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApiModule {
    @Named("url_api")
    @Provides
    fun provideBaseUrlProd(): String = "https://api.flickr.com/services/rest/"

    @Singleton
    @Provides
    fun provideApi(@Named("url_api") baseUrl: String): Client =
        Retrofit.Builder()
            .baseUrl(baseUrl)
//            .client(
//                OkHttpClient.Builder()
//                    .addInterceptor(MyInterceptor)
//                    .build()
//            )
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(Client::class.java)

    private val gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
       // .excludeFieldsWithoutExposeAnnotation()
        .create()
}