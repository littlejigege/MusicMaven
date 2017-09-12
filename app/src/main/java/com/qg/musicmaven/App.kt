package com.qg.musicmaven

import android.app.Application
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.strategy.SocketInternetObservingStrategy
import java.io.File
import kotlin.properties.Delegates
import com.jimji.preference.Preference
import com.qg.musicmaven.netWork.KuGouApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
        retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://songsearch.kugou.com/")
                .build()
    }
}