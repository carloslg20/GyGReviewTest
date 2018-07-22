package com.cleon.gygreviews.gygreviews.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.cleon.gygreviews.gygreviews.model.Review
import com.cleon.gygreviews.gygreviews.repository.ReviewRepository
import com.cleon.gygreviews.gygreviews.vo.Resource
import com.cleon.gygreviews.gygreviews.vo.Status


class ReviewsViewModel(application: Application, private val viewRepository: ReviewRepository) : AndroidViewModel(application) {

    val currentPage: MutableLiveData<Int> = MutableLiveData()

    private val mediatorData = MediatorLiveData<Resource<List<Review>>>()

    val allData: MutableLiveData<MutableList<Review>> = MutableLiveData()

    val list: LiveData<Resource<List<Review>>> = Transformations
            .switchMap(currentPage) { _ ->
                viewRepository.loadReviews(currentPage.value!!)
            }

    init {
//        Transformations.switchMap(list) { input ->
//            if (input.status == Status.SUCCESS) {
//                val list =  allData.value
//                list?.addAll(input.data?: emptyList())
//                allData.value = list
//            }
//             allData
//        }
//        mediatorData.value = Resource.loading(null)
//        mediatorData.addSource(list) { input ->
//            if (input.status == Status.SUCCESS) {
//                val list =  allData.value
//                list?.addAll(input.data?: emptyList())
//                allData.value = list
//            }
//        }

        currentPage.value = 0
    }

    fun addReviewData(resource: Resource<List<Review>>) {
        if (resource.status == Status.SUCCESS) {
            val list = allData.value ?: mutableListOf()
            list?.addAll(resource.data ?: emptyList())
            allData.value = list
        }
    }


    /**
     * Factory class to inject the ReviewRepository into the ViewModel
     */
    class Factory(private val application: Application, private val reviewRepository: ReviewRepository) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ReviewsViewModel(application, reviewRepository) as T
        }
    }

}
