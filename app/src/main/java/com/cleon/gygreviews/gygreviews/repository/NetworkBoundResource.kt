package com.cleon.gygreviews.gygreviews.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.cleon.gygreviews.gygreviews.AppExecutors
import com.cleon.gygreviews.gygreviews.api.ApiResponse
import com.cleon.gygreviews.gygreviews.vo.Resource

/**
 *  https://developer.android.com/jetpack/docs/guide#addendum
 */
abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread constructor(private val appExecutors: AppExecutors) {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
        }
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when (response) {
                is ApiResponse.ApiSuccessResponse -> {
                    appExecutors.diskIO().execute {
                        saveCallResult(processResponse(response))
                        appExecutors.mainThread().execute {
                            // we specially request a new live data,
                            // otherwise we will get immediately last cached value,
                            // which may not be updated with latest results received from network.
                            result.addSource(loadFromDb()) { newData ->
                                setValue(Resource.success(newData))
                            }
                        }
                    }
                }
                is ApiResponse.ApiErrorResponse -> {
                    onFetchFailed()
                    result.addSource(dbSource) { newData ->
                        setValue(Resource.error(response.errorMessage, newData))
                    }
                }
            }
        }
    }

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    @MainThread
    protected open fun onFetchFailed() {}

    // returns a LiveData that represents the resource, implemented
    // in the base class.
    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected open fun processResponse(response: ApiResponse.ApiSuccessResponse<RequestType>) = response.body

    // // Called to save the result of the API response into the database
    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    // Called with the data in the database to decide whether it should be
    // fetched from the network.
    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    // Called to get the cached data from the database
    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    // Called to create the API call.
    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}
