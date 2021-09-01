package ru.neosvet.flickr.api

import okhttp3.Interceptor
import okhttp3.Response
import ru.neosvet.flickr.FLICKR_API_KEY

object MyInterceptor : Interceptor {

    private const val CONST_PART = "&format=json&nojsoncallback=1&api_key=$FLICKR_API_KEY"

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain.request()
                .newBuilder()
                .url(chain.request().url.toString() + CONST_PART)
                .build()
        )

}