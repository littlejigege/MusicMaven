package com.qg.musicmaven.modle

import android.support.annotation.WorkerThread
import com.mobile.utils.preference
import com.mobile.utils.toast
import com.qg.musicmaven.modle.bean.ServerAudio
import com.qiniu.android.http.ResponseInfo
import com.qiniu.android.storage.*
import com.qiniu.common.Zone
import com.qiniu.storage.BucketManager
import com.qiniu.util.Auth
import org.json.JSONObject
import java.io.File

/**
 * Created by jimiji on 2017/12/3.
 */
object QiNiu {
    var accessKey = "JTzzyirY3g84GgVl-LsFePbusNOx1xWjWp-oLEMl"
    var secretKey = "Rw1vCPSY5gfn-ia__vhSt9GwofLu501V_Ecr6wzl"
    var bucket = "wilderg"
    //上传凭证，七牛要求的，痕迹吧无语
    private val TOKEN: String
        get() = Auth.create(accessKey, secretKey).uploadToken(bucket)

    private val upLoadManager: UploadManager = UploadManager(Configuration.Builder().build())

    //异步上传
    fun upLoad(filePath: String, progressHandler: UpProgressHandler,cancellationSignal: UpCancellationSignal) {
        upLoadManager.put(filePath, filePath.substring(filePath.lastIndexOf("/") + 1), TOKEN, { key, info, _ ->
            if (!info.isOK) {
                info.error.toast()
            } else {

            }

        }, UploadOptions(null, null, false, progressHandler, cancellationSignal))
    }

    @WorkerThread
    fun getAllFile(): MutableList<ServerAudio> {
        val list = mutableListOf<ServerAudio>()
        val it = BucketManager(Auth.create(accessKey, secretKey), com.qiniu.storage.Configuration(Zone.autoZone())).createFileListIterator(bucket, "")
        while (it.hasNext()) {
            list.addAll(it.next().map { ServerAudio("", "http://ozwr11exu.bkt.clouddn.com/${it.key}", it.key, it.putTime.toString()) })
        }
        return list
    }
}