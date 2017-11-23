package com.qg.musicmaven.netWork

import android.app.Activity
import android.app.DownloadManager
import android.net.Uri
import android.util.Log

import com.mobile.utils.Preference
import com.mobile.utils.downloadManager
import com.mobile.utils.showToast
import com.qg.musicmaven.App
import com.qg.musicmaven.download.DownloadCallback
import com.qg.musicmaven.download.DownloadUtil
import com.qg.musicmaven.modle.Audio
import com.qg.musicmaven.modle.AudioInfo
import com.qg.musicmaven.modle.FeedBack
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import okhttp3.MediaType
import okhttp3.RequestBody

import java.io.File
import java.net.URI
import java.net.URL

/**
 * Created by jimji on 2017/9/13.
 */
class DownLoadActionCreator(var act: Activity) : ActionCreator() {
    val utils by lazy { DownloadUtil(downloadManager) }
    private fun getAudio(hash: String, albumId: String, callback: DownloadCallback) {
        App.kugouApi.getAudio(hash, albumId)
                .subscribeOn(IoScheduler())
                .observeOn(IoScheduler())
                .subscribe(object : Observer<FeedBack<Audio>> {
                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onComplete() {

                    }

                    override fun onNext(t: FeedBack<Audio>) {
                        if (!t.data.playUrl.isEmpty()) {
                            App.serverApi.postSong(RequestBody.create(MediaType.parse("application/json"), JsonMaker.make {
                                objects {
                                    "userId" - Preference.get("user", "userId" to -1L)
                                    "singerName" - t.data.audioName.split("-")[0]
                                    "songName" - t.data.audioName.split("-")[1]
                                    "imgUrl" - t.data.imgUrl
                                    "playUrl" - t.data.playUrl
                                }
                            }))
                                    .subscribeOn(IoScheduler())
                                    .subscribe(object: Observer<FeedBack<Int>> {
                                        override fun onComplete() {
                                        }
                                        override fun onSubscribe(d: Disposable) {
                                        }
                                        override fun onError(e: Throwable) {
                                        }
                                        override fun onNext(t: FeedBack<Int>) {
                                        }
                                    })
                        }
                        startDownload(t.data.playUrl, t.data.audioName, callback)
                    }
                })
    }

    fun checkApk() {
        utils.checkForApk(act)
    }

    fun download(info: AudioInfo, isHQ: Boolean, callback: DownloadCallback) {
        getAudio(if (isHQ) info.hqFileHash else info.fileHash, info.albumId, callback)
    }

    private fun startDownload(url: String, fileName: String, callback: DownloadCallback) {
        if (url.isEmpty()) {
            showToast("无法下载")
            return
        }
//        val request = DownloadManager.Request(Uri.parse(url))
//        request.setTitle(fileName).setDescription("MusicMaven")
//                .setDestinationUri(Uri.parse("file://${App.DOWNLOAD_PATH}/$fileName.mp3"))
//        act.downloadManager.enqueue(request)
        if (File("${App.DOWNLOAD_PATH}/$fileName.mp3").exists()) {
            showToast("文件已存在")
            return
        }
        utils.download(url, fileName, callback)
//        FileDownloader.getImpl().create(url)
//                .setPath(App.DOWNLOAD_PATH)
//                .setListener(object : FileDownloadListener() {
//                    override fun warn(task: BaseDownloadTask?) {
//
//                    }
//
//                    override fun completed(task: BaseDownloadTask?) {
//
//                    }
//
//                    override fun pending(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
//
//                    }
//
//                    override fun error(task: BaseDownloadTask?, e: Throwable) {
//                        e.printStackTrace()
//                    }
//
//                    override fun progress(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
//                        Log.d("asd", (soFarBytes / totalBytes).toString())
//                    }
//
//                    override fun paused(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
//
//                    }
//                }).start()
    }
}