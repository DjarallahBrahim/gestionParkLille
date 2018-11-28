package com.example.samlille.gestiondepark.DataBase

import android.os.Handler
import android.os.HandlerThread

/**
 * DbWorkerThread: we execute our Database request in threads
 */
class DbWorkerThread(threadName: String) : HandlerThread(threadName) {

    private lateinit var mWorkerHandler: Handler

    override fun onLooperPrepared() {
        super.onLooperPrepared()
        mWorkerHandler = Handler(looper)
    }

    fun postTask(task: Runnable) {
        mWorkerHandler.post(task)
    }

}