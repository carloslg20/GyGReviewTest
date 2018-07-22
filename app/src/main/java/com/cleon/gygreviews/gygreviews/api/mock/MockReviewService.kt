package com.cleon.gygreviews.gygreviews.api.mock

import androidx.lifecycle.LiveData
import com.cleon.gygreviews.gygreviews.api.ApiResponse
import com.cleon.gygreviews.gygreviews.api.ReviewService
import com.cleon.gygreviews.gygreviews.model.ReviewResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MockReviewService  {

    @GET("api/review/reviews")
    fun getReviews(@Query("page") page: Int): LiveData<ApiResponse<ReviewResponse>>

}