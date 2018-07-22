package com.cleon.gygreviews.gygreviews.api

import androidx.lifecycle.LiveData
import com.cleon.gygreviews.gygreviews.model.ReviewResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ReviewService {

    @GET("/berlin-l17/tempelhof-2-hour-airport-history-tour-berlin-airlift-more-t23776/reviews.json")
    fun getReviews(@Query("count") count: Int = 5,
                   @Query("page") page: Int = 0,
                   @Query("rating") rating: Int = 0,
                   @Query("type") type: String = "",
                   @Query("sortBy") sortBy: String = "date_of_review",
                   @Query("direction") direction: String = "DESC"): LiveData<ApiResponse<ReviewResponse>>

    //Testing - Temporal Solution
    @GET("api/review")
    fun getMockReviews(@Query("page") page: Int): LiveData<ApiResponse<ReviewResponse>>

}
