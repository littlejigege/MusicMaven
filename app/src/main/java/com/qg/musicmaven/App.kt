package com.qg.musicmaven

import android.app.Application
import android.media.MediaPlayer
import android.support.v4.media.session.MediaControllerCompat
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechUtility
import com.mobile.utils.*

import java.io.File
import kotlin.properties.Delegates

import com.qg.musicmaven.download.ApkDownloadUtil
import com.qg.musicmaven.modle.bean.User
import com.qg.musicmaven.netWork.CacheInterceptor
import com.qg.musicmaven.netWork.KuGouApi
import com.qg.musicmaven.netWork.ServerApi


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import okhttp3.Cache


/**
 * Created by 小吉哥哥 on 2017/9/7.
 */


class App : Application() {
    lateinit var retrofit: Retrofit
    //音乐播放控制器
    var musicController: MediaControllerCompat? = null

    companion object {
        const val SERVER_ADDRESS = "http://120.77.38.183:8080/gaojiancheng.mavenmusic/"
        const val QIQUI_ADDRESS = "http://ozwr11exu.bkt.clouddn.com"
        val player by lazy { MediaPlayer() }
        var instance: App by Delegates.notNull()
        val kugouApi by lazy { App.instance.retrofit.create(KuGouApi::class.java) }
        val serverApi by lazy { App.instance.retrofit.create(ServerApi::class.java) }
        val DOWNLOAD_PATH: String
            get() {
                //把下载路径从默认的preference里面拿出来
                val path = Preference.get("${instance.packageName}_preferences", "PATH" to "/storage/emulated/0/MusicMaven") as String
                val file = File(path)
                file.toggleDir()
                return path
            }
        var uuid: String? = null
        val isNoPic: Boolean
            get() {
                return Preference.get("${instance.packageName}_preferences", "IMAGE" to false) as Boolean
            }

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Utils.init(this)
        buildRetrofit()
        setFooter()
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5a16dc2a")
        //saveUser(User("845956752@qq.com", "城", "123456", 3))

    }

    /**
     * 构建网络
     */
    private fun buildRetrofit() {

        //构建带缓存的client
        val size = (1024 * 1024 * 8).toLong()//缓存大小为8Mb
        val cache = Cache(File(cacheDir, "a"), size)
        val client = OkHttpClient
                .Builder()
                .addInterceptor(CacheInterceptor())
                .addNetworkInterceptor(CacheInterceptor())
                .cache(cache)
                .build()
        //构建retrofit
        retrofit = Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://songsearch.kugou.com/")
                .build()
    }

    private fun setFooter() {
        SmartRefreshLayout.setDefaultRefreshFooterCreater { context, layout ->
            //指定为经典Footer，默认是 BallPulseFooter
            BallPulseFooter(context)
        }
    }

    fun getUser(): User? {
        val user = Preference.get("user", "user" to
                User("", "", "", -1)) as User
        return if (user.userId == -1) null else user
    }

    fun saveUser(user: User) {
        Preference.save("user") {
            "user" - user
        }
    }

    fun playAudio(url: String, songName: String, singer: String, imageUrl: String) {
        MyMusicService.url = url
        MyMusicService.songName = songName
        MyMusicService.singer = singer
        MyMusicService.imageUrl = imageUrl
        musicController?.transportControls?.play()
    }

    fun stopAudio() {
        musicController?.transportControls?.stop()

    }

    fun hasUser() = getUser() != null
}