package com.qg.musicmaven.netWork

import com.mobile.utils.isConnected
import com.qg.musicmaven.utils.ping
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * Created by jimiji on 2017/12/15.
 */
class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val response: Response
        val response2: Response
        if (isConnected()) {
            println(request)
            response = chain.proceed(request)
            val cacheControl = CacheControl.Builder()
                    .build()
                    .toString()
            response2 = response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", cacheControl)
                    .build()
        } else {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
            response2 = chain.proceed(request)
        }
        return response2
    }


}