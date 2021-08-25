package ru.neosvet.flickr.api

import okhttp3.Interceptor
import okhttp3.Response

object MyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        println("Response:")
        println("Code: " + response.code())
        println("Body: " + response.body()?.string())
        return response
    }

}