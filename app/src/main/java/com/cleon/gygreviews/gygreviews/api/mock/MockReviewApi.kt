package com.cleon.gygreviews.gygreviews.api.mock

import android.content.Context
import android.util.Log
import com.cleon.gygreviews.gygreviews.util.LiveDataCallAdapterFactory
import ir.mirrajabi.okhttpjsonmock.OkHttpMockInterceptor
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream

class MockReviewApi {

    companion object {

        private const val BASE_URL = "https://www.test.com/"

        fun create(function: (R : String) -> InputStream) = create(HttpUrl.parse(BASE_URL)!!, function)

        private fun create(httpUrl: HttpUrl, function: (R : String) -> InputStream): Retrofit {
            val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                Log.d("API", it)
            })
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client =  OkHttpClient.Builder()
                    .addInterceptor(OkHttpMockInterceptor(function, 5))
                    .addInterceptor(logger)
                    .build()


            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addCallAdapterFactory(LiveDataCallAdapterFactory())
                    .baseUrl(httpUrl)
                    .client(client)
                    .build()
        }

    }
}