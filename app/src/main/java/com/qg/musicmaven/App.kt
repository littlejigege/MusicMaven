package com.qg.musicmaven

import android.app.Application
import android.media.MediaPlayer
import com.mobile.utils.ActivityManager

import java.io.File
import kotlin.properties.Delegates

import com.mobile.utils.Preference
import com.mobile.utils.Utils
import com.mobile.utils.showToast

import com.qg.musicmaven.download.DownloadUtil
import com.qg.musicmaven.netWork.KuGouApi
import com.qg.musicmaven.netWork.ServerApi


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by 小吉哥哥 on 2017/9/7.
 */
class App : Application() {
    lateinit var retrofit: Retrofit

    companion object {
        val player by lazy { MediaPlayer() }
        var instance: App by Delegates.notNull()
        val kugouApi by lazy { App.instance.retrofit.create(KuGouApi::class.java) }
        val serverApi by lazy { App.instance.retrofit.create(ServerApi::class.java) }
        var DOWNLOAD_PATH: String
            set(value) {
                Preference.save("PATH") { "PATH" - value }
            }
            get() {
                val path = Preference.get("PATH", "PATH" to "/storage/emulated/0/MusicMaven") as String
                val file = File(path)
                if (!file.exists()) {
                    file.mkdirs()
                }
                return path
            }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        DownloadUtil.init(this)
        Utils.init(this)
        buildRetrofit()
    }

    /**
     * 构建网络
     */
    private fun buildRetrofit() {
        val client = OkHttpClient.Builder().build()
        retrofit = Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://songsearch.kugou.com/")
                .build()
    }
}