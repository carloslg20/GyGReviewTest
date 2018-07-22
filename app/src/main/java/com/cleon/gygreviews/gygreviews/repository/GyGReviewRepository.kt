package com.cleon.gygreviews.gygreviews.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.cleon.gygreviews.gygreviews.AppExecutors
import com.cleon.gygreviews.gygreviews.api.ApiResponse
import com.cleon.gygreviews.gygreviews.api.ReviewService
import com.cleon.gygreviews.gygreviews.db.ReviewDao
import com.cleon.gygreviews.gygreviews.model.Review
import com.cleon.gygreviews.gygreviews.model.ReviewResponse
import com.cleon.gygreviews.gygreviews.vo.Resource


class GyGReviewRepository(private val appExecutors: AppExecutors,
                          private val reviewDao: ReviewDao,
                          private val reviewService: ReviewService) : ReviewRepository {


    override fun loadReviews(page: Int): LiveData<Resource<List<Review>>> {

        return object : NetworkBoundResource<List<Review>, ReviewResponse>(appExecutors) {

            override fun createCall(): LiveData<ApiResponse<ReviewResponse>> {
                return reviewService.getMockReviews(page = page)
            }

            override fun loadFromDb(): LiveData<List<Review>> {
                return Transformations.map(reviewDao.getAll()) { it -> it }
            }
//                return Transformations.switchMap(reviewDao.getAll()) { searchData ->
//                    if (searchData == null) {
//                        AbsentLiveData.create()
//                    } else {
//                        reviewDao.getAll()
//                    }
//                }
//            }

            override fun shouldFetch(data: List<Review>?): Boolean = true

            override fun saveCallResult(item: ReviewResponse) {
                val listReview = item.data
                reviewDao.insertReviews(listReview)
            }
        }.asLiveData()
    }

}