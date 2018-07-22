package com.cleon.gygreviews.gygreviews.api

import androidx.lifecycle.LiveData
import com.cleon.gygreviews.gygreviews.model.NewReview
import com.cleon.gygreviews.gygreviews.model.NewReviewResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface PostReviewService {

    @POST("reviews/berlin-tour/new")
    fun createPost(@Body review: NewReview): LiveData<ApiResponse<NewReviewResponse>>
}