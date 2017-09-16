package com.qg.musicmaven.download

import android.app.DownloadManager

/**
 * Created by jimji on 2017/9/14.
 */
interface DownloadCallback {
    fun onPaused()
    fun onPending()
    fun onRunning()
    fun onSuccessful()
    fun onFailed()
}