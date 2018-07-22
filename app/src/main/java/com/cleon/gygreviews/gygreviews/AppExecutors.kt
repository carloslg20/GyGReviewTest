package com.cleon.gygreviews.gygreviews

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

open class AppExecutors {

    private val diskIO: Executor
    private val networkIO: Executor
    private val mainThread: Executor

    init {
        diskIO = Executors.newSingleThreadExecutor()
        networkIO = Executors.newFixedThreadPool(3)
        mainThread = MainThreadExecutor()
    }

    fun diskIO(): Executor {
        return diskIO
    }

    fun networkIO(): Executor {
        return networkIO
    }

    fun mainThread(): Executor {
        return mainThread
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }

    companion object {

        private var INSTANCE: AppExecutors? = null

        fun getInstance(): AppExecutors? {
            if (INSTANCE == null) {
                synchronized(AppExecutors::class) {
                    INSTANCE = AppExecutors()
                }
            }
            return INSTANCE
        }
    }

}
