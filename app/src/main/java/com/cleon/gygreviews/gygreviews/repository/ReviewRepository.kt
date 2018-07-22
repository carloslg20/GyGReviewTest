package com.cleon.gygreviews.gygreviews.repository

import androidx.lifecycle.LiveData
import com.cleon.gygreviews.gygreviews.model.Review
import com.cleon.gygreviews.gygreviews.vo.Resource

interface ReviewRepository {

    fun loadReviews(page: Int): LiveData<Resource<List<Review>>>

}