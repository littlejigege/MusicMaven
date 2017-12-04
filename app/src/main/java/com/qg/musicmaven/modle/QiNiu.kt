package com.qg.musicmaven.modle

import com.mobile.utils.preference
import com.qiniu.android.http.ResponseInfo
import com.qiniu.android.storage.Configuration
import com.qiniu.android.storage.UpCompletionHandler
import com.qiniu.android.storage.UploadManager
import org.json.JSONObject
import java.io.File

/**
 * Created by jimiji on 2017/12/3.
 */
object QiNiu {
    //上传凭证，七牛要求的，痕迹吧无语
    private val TOKEN: String by preference("qiniu", "token" to "")
    private val upLoadManager: UploadManager = UploadManager(Configuration.Builder().build())
    /**
     * 凭证是否有效
     */
    fun isTokenValid(): Boolean {
        return !TOKEN.isEmpty()
    }

    fun upLoad() {
        upLoadManager.put(byteArrayOf(12,2,3,2,3,24,4), null, "JTzzyirY3g84GgVl-LsFePbusNOx1xWjWp-oLEMl:ovWwk5-Hx3JY_P-6pDgfwtpj_A0=:eyJzY29wZSI6IndpbGRlcmciLCJkZWFkbGluZSI6MTUxMjI5NDc4M30=", { key, info, response ->
            if (info.isOK) {
                if (info.statusCode == ResponseInfo.InvalidToken) {

                }
            }

        }, null)
    }
}