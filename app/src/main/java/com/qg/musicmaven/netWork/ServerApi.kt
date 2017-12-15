package com.qg.musicmaven.netWork

import com.qg.musicmaven.BuildConfig
import com.qg.musicmaven.modle.bean.FeedBack
import com.qg.musicmaven.modle.bean.RegisterBody
import com.qg.musicmaven.modle.bean.ServerAudio
import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by jimji on 2017/9/14.
 */
interface ServerApi {
    @POST("http://120.77.38.183:8080/gaojiancheng.mavenmusic/user/login")
    fun login(@Body body: RequestBody): Observable<FeedBack<Long>>

    @GET("http://120.77.38.183:8080/gaojiancheng.mavenmusic/download/detail")
    fun getsonglist(@Query("userId") id: Long): Observable<FeedBack<MutableList<ServerAudio>>>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("http://120.77.38.183:8080/gaojiancheng.mavenmusic/download/songs")
    fun postSong(@Body body: RequestBody): Observable<FeedBack<Int>>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("http://120.77.38.183:8080/gaojiancheng.mavenmusic/user/verify")
    fun register(@Body body: RequestBody): Observable<FeedBack<Int>>

    @GET("http://120.77.38.183:8080/gaojiancheng.mavenmusic/user/getcount")
    fun getCode(@Query("userEmail") email: String): Call<ResponseBody>

    @GET("http://120.77.38.183:8080/gaojiancheng.mavenmusic/update/version")
    fun checkApk(@Query("versionCode") versionCode: Int = BuildConfig.VERSION_CODE): Observable<FeedBack<String>>

    @POST("http://120.77.38.183:8080/gaojiancheng.mavenmusic/user/register")
    fun register(@Body body: RegisterBody):Observable<FeedBack<String>>
}