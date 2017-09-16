package com.qg.musicmaven

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.strategy.SocketInternetObservingStrategy
import java.io.File
import kotlin.properties.Delegates
import com.jimji.preference.Preference
import com.liulishuo.filedownloader.FileDownloader
import com.qg.musicmaven.download.DownloadUtil
import com.qg.musicmaven.netWork.KuGouApi
import com.qg.musicmaven.netWork.ServerApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import utils.showToast
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by 小吉哥哥 on 2017/9/7.
 */
class App : Application() {
    companion object {
        var instance: App by Delegates.notNull()
        var retrofit: Retrofit by Delegates.notNull()
        val kugouApi by lazy { App.retrofit.create(KuGouApi::class.java) }
        val serverApi by lazy { App.retrofit.create(ServerApi::class.java) }
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
        //init my utils
        Preference.init(this)
        //ObserveNetWork
        startObserveNetWork()
        //build network core
        buildRetrofit()
        FileDownloader.setupOnApplicationOnCreate(this)
        DownloadUtil.init(this)
        Utils.init(this)
        Stetho.initializeWithDefaults(this)

    }

    private fun startObserveNetWork() {
        ReactiveNetwork.observeInternetConnectivity(SocketInternetObservingStrategy(), "www.baidu.com")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it) {

                    } else {

                    }
                }
    }

    private fun buildRetrofit() {
        val client = OkHttpClient.Builder().addNetworkInterceptor(StethoInterceptor()).build()

        retrofit = Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://songsearch.kugou.com/")
                .build()
    }
}