package com.cleon.gygreviews.gygreviews.api

import android.util.Log
import com.cleon.gygreviews.gygreviews.util.LiveDataCallAdapterFactory
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ReviewApi {

    companion object {

        private const val BASE_URL = "https://www.getyourguide.com/"

        fun create() = create(HttpUrl.parse(BASE_URL)!!)

        private fun create(httpUrl: HttpUrl): Retrofit {
            val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                Log.d("API", it)
            })
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()


            return Retrofit.Builder()
                    .baseUrl(httpUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(LiveDataCallAdapterFactory())
                    .build()
        }
    }
}