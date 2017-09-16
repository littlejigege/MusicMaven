package com.qg.musicmaven.netWork

import com.qg.musicmaven.modle.FeedBack
import com.qg.musicmaven.modle.ServerAudio
import io.reactivex.Observable
import io.reactivex.Observer
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by jimji on 2017/9/14.
 */
interface ServerApi {
    @POST()
    @FormUrlEncoded
    fun login(@Field("email") email: String, @Field("password") password: String): Observable<FeedBack<Int>>

    @GET()
    fun getsonglist(@Query("userId") id: Long): Observable<FeedBack<MutableList<ServerAudio>>>

    @POST()
    @FormUrlEncoded
    fun postSong(@Field("userId") id: Long, @Field("audioName") audioName: String, @Field("imgUrl") imgUrl: String, @Field("playUrl") playUr: String): Observable<FeedBack<Int>>

    @POST("http://192.168.199.119:8080/user/register")
    @FormUrlEncoded
    fun register(@Body json: String): Observable<FeedBack<Int>>

    @GET("http://192.168.199.119:8080/user/getcount?")
    fun getCode(@Query("userEmail") email: String): Call<ResponseBody>

    @GET()
    fun checkApk(): Observable<FeedBack<Int>>
}