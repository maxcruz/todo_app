package com.example.todoapp.util

import android.os.Handler
import android.os.Looper
import android.support.annotation.VisibleForTesting
import java.util.concurrent.Executor

open class AppExecutors @VisibleForTesting constructor(private val diskIO: Executor,
                                                       private val mainThread: Executor) {

    constructor() : this(DiskIOThreadExecutor(), MainThreadExecutor())

    fun diskIO(): Executor {
        return diskIO
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

}
