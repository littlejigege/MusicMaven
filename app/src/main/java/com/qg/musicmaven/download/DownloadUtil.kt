package com.qg.musicmaven.download

import android.app.Activity
import android.app.Application
import android.app.DownloadManager
import android.net.Uri
import android.util.Log

import com.mobile.utils.inUiThread
import com.mobile.utils.permission.Permission
import com.qg.musicmaven.App
import com.qg.musicmaven.modle.bean.FeedBack
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import java.io.File

/**
 * Created by jimji on 2017/9/13.
 */
class DownloadUtil(val manager: DownloadManager) {
    private var callbackMap = mutableMapOf<Long, DownloadCallback>()
    private var thread: Thread? = null
    private var apkId = 0L

    companion object {
        lateinit var app: Application
        fun init(app: Application) {
            this.app = app
        }
    }


    fun download(url: String, fileName: String, callback: DownloadCallback) {
        val request = DownloadManager.Request(Uri.parse(url))
        request.setTitle(fileName).setDescription("MusicMaven")
                .setDestinationUri(Uri.parse("file://${App.DOWNLOAD_PATH}/$fileName.mp3"))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        callbackMap.put(manager.enqueue(request), callback)
    }

    fun downloadApk(url: String) {
        val request = DownloadManager.Request(Uri.parse(url))
        request.setDestinationUri(Uri.parse("file://${App.DOWNLOAD_PATH}/FreeMusic.apk"))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        apkId = manager.enqueue(request)
    }


    private fun isApkfinish(): Boolean {
        val query = DownloadManager.Query()
        query.setFilterById(apkId)
        val cursor = manager.query(query)
        return if (cursor.moveToFirst())
            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL
        else false
    }

    fun checkForApk(act: Activity) {
        App.serverApi.checkApk()
                .subscribeOn(IoScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<FeedBack<String>> {
                    override fun onNext(value: FeedBack<String>) {
                        Log.d("Asdasd", value.data)
                        if (!value.data.isEmpty()) {
                            Permission.STORAGE.doAfterGet(act){
                                File("${App.DOWNLOAD_PATH}/FreeMusic.apk").delete()
                                inUiThread {

                                }
                            }

                        }
                        //TODo 判断石佛偶有新的
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                    override fun onSubscribe(d: Disposable) {

                    }


                    override fun onComplete() {

                    }
                })

    }


    private fun checkStatus() {
        callbackMap.forEach {
            val query = DownloadManager.Query()
            query.setFilterById(it.key)
            val cursor = manager.query(query)
            if (cursor.moveToFirst()) {
                when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                    DownloadManager.STATUS_PAUSED -> {
                        it.value.onPaused()
                    }
                    DownloadManager.STATUS_PENDING -> {
                        it.value.onPending()
                    }
                    DownloadManager.STATUS_FAILED -> {
                        it.value.onFailed()
                        callbackMap.remove(it.key)
                    }
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        it.value.onSuccessful()
                        callbackMap.remove(it.key)
                    }
                    DownloadManager.STATUS_RUNNING -> {
                        it.value.onRunning()
                    }
                }
            }
        }

    }

}