package com.qg.musicmaven.netWork

import com.qg.musicmaven.App
import com.qg.musicmaven.BuildConfig
import com.qg.musicmaven.modle.bean.*
import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by jimji on 2017/9/14.
 */
interface ServerApi {
    @POST("${App.SERVER_ADDRESS}/user/login")
    fun login(@Body body: RequestBody): Observable<FeedBack<User>>

    @GET("${App.SERVER_ADDRESS}/download/detail")
    fun getsonglist(@Query("userId") id: Long, @Query("pageNum") page: Int = 1, @Query("pageSize") count: Int = 10): Observable<FeedBack<MutableList<ServerAudio>>>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("${App.SERVER_ADDRESS}/song/upload")
    fun postSong(@Body body: RequestBody): Observable<FeedBack<Int>>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("${App.SERVER_ADDRESS}/download/songs")
    fun postKGSong(@Body body: RequestBody): Observable<FeedBack<Int>>

    @POST("${App.SERVER_ADDRESS}/user/register")
    fun register(@Body body: RegisterBody): Observable<FeedBack<String>>

    @GET("${App.SERVER_ADDRESS}/user/getcount")
    fun getCode(@Query("userEmail") email: String): Observable<Any>

    @GET("${App.SERVER_ADDRESS}/update/version")
    fun checkApk(@Query("versionCode") versionCode: Int = BuildConfig.VERSION_CODE): Observable<FeedBack<String>>

    @GET("${App.SERVER_ADDRESS}/song/servermusic")
    fun getSinger(@Query("pageNum") page: Int = 1, @Query("pageSize") count: Int = 10): Observable<FeedBack<MutableList<Singer>>>

    @GET("${App.SERVER_ADDRESS}/song/servermusic")
    fun getSingerSong(@Query("singerName") singerName: String, @Query("pageNum") page: Int = 1
                      , @Query("pageSize") count: Int = 10): Observable<FeedBack<MutableList<ServerAudio>>>

    @GET("${App.SERVER_ADDRESS}/song/wantlist")
    fun getWishList(@Query("userId") userId: Int = -1, @Query("pageNum") page: Int = 1, @Query("pageSize") count: Int = 10): Observable<FeedBack<MutableList<Wish>>>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("${App.SERVER_ADDRESS}/song/desire")
    fun postWish(@Body body: RequestBody): Observable<FeedBack<Int>>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("${App.SERVER_ADDRESS}/song/achievedream")
    fun achieveDream(@Body body: RequestBody): Observable<FeedBack<Int>>

    @GET("${App.SERVER_ADDRESS}/song/search")
    fun searchSong(@Query("information") keyWord: String, @Query("pageNum") page: Int = 1, @Query("pageSize") count: Int = 10): Observable<FeedBack<MutableList<ServerAudio>>>
}