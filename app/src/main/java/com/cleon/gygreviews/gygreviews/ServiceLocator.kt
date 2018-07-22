package com.cleon.gygreviews.gygreviews

import android.app.Application
import android.content.Context
import com.cleon.gygreviews.gygreviews.api.mock.MockReviewService
import com.cleon.gygreviews.gygreviews.api.ReviewApi
import com.cleon.gygreviews.gygreviews.api.ReviewService
import com.cleon.gygreviews.gygreviews.api.mock.MockReviewApi
import com.cleon.gygreviews.gygreviews.db.AppDataBase
import com.cleon.gygreviews.gygreviews.repository.GyGReviewRepository
import com.cleon.gygreviews.gygreviews.repository.ReviewRepository
import com.cleon.gygreviews.gygreviews.util.LiveDataCallAdapterFactory
import ir.mirrajabi.okhttpjsonmock.OkHttpMockInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream


interface ServiceLocator {
    companion object {
        private val LOCK = Any()
        private var instance: ServiceLocator? = null
        fun instance(context: Context): ServiceLocator {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = DefaultServiceLocator(context.applicationContext as Application)
                }
                return instance!!
            }
        }
    }

    fun getRepository(): ReviewRepository

    fun getReviewService(): ReviewService

    // TODO fix later
    fun getMockReviewService(): ReviewService

    fun getMockRepository(): ReviewRepository
}


open class DefaultServiceLocator(val app: Application) : ServiceLocator {

    private val api = ReviewApi.create()
    private val mockApi = MockReviewApi.create {  path -> app.assets.open(path)  }
    private val db = AppDataBase.getInstance(app)!!


    override fun getRepository(): ReviewRepository {
        return GyGReviewRepository(AppExecutors.getInstance()!!, db.reviewDataDao(), getReviewService())
    }

    override fun getReviewService(): ReviewService {
        return api.create(ReviewService::class.java)
    }

    // TODO fix relationship between real and mock service
    override fun getMockReviewService(): ReviewService {
        return mockApi.create(ReviewService::class.java)
    }

    override fun getMockRepository(): ReviewRepository {
        return GyGReviewRepository(AppExecutors.getInstance()!!, db.reviewDataDao(), getMockReviewService())
    }

}