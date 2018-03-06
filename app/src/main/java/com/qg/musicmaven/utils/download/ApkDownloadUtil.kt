package com.qg.musicmaven.download

import android.app.Activity
import android.app.Application
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.util.Log

import com.mobile.utils.inUiThread
import com.mobile.utils.permission.Permission
import com.mobile.utils.showToast
import com.qg.musicmaven.App
import com.qg.musicmaven.modle.bean.FeedBack
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import java.io.File
import android.support.v4.content.FileProvider
import android.os.Build
import android.os.Environment


/**
 * Created by jimji on 2017/9/13.
 */
class ApkDownloadUtil(val manager: DownloadManager) {
    private var apkId = 0L
    private var receiver: DownloadDoneReceiver? = null

    fun downloadApk(url: String) {
        initReceiver()
        val request = DownloadManager.Request(Uri.parse(url))
        val file = File(App.instance.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),"Maven.apk")
        request.setDestinationUri(Uri.fromFile(file))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        apkId = manager.enqueue(request)
    }

    private fun initReceiver() {
        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        receiver = DownloadDoneReceiver()
        App.instance.registerReceiver(receiver, intentFilter)
    }

    inner class DownloadDoneReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == apkId) {
                installApk(App.instance, "${App.instance.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)}/Maven.apk")
                context.unregisterReceiver(receiver)
            }
        }
    }

    private fun installApk(context: Context, apkPath: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val file = File(apkPath)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val apkUri = FileProvider.getUriForFile(context, "com.qg.musicmaven.fileProvider", file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(File(apkPath)),

                    "application/vnd.android.package-archive")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}