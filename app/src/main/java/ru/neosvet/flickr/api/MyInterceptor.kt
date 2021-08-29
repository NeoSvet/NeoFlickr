package ru.neosvet.flickr.api

import okhttp3.Interceptor
import okhttp3.Response

object MyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        println("Response:")
        println("Url: " + response.request().url())
        println("Code: " + response.code())
        val body = response.body()?.string()
        body?.let {
            if (it.length < 4000)
                println("Body: " + it)
            else {
                println("Body:")
                var i = 0
                var k = 4000
                while (i < it.length) {
                    if (k > it.length)
                        println(it.substring(i))
                    else
                        println(it.substring(i, k))
                    i += 4000
                    k += 4000
                }
            }
        }
        //println("Body: " + response.body()?.string())
        return response
    }

}