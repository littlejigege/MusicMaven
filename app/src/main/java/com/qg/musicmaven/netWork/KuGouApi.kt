package com.qg.musicmaven.netWork

import com.qg.musicmaven.modle.bean.*
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * 存放酷狗相关的api
 * Created by 小吉哥哥 on 2017/9/7.
 */
interface KuGouApi {
    //关键字搜索jQuery112401895726466394836_1504849944559
    @GET("http://songsearch.kugou.com/song_search_v2?callback=&pagesize=15&userid=-1&clientver=&platform=WebFilter&tag=em&filter=2&iscorrection=1&privilege_filter=0&_=1504849944561")
    fun getAudioInfoList(@Query("keyword") keyWord: String, @Query("page") page: Int = 1): Observable<FeedBack<AudioInfoContainer>>

    //根据hash得到audio相关下载地址
    @GET("http://www.kugou.com/yy/index.php?r=play/getdata")
    fun getAudio(@Query("hash") hash: String, @Query("album_id") albumId: String): Observable<FeedBack<Audio>>

    //获得建议
    @GET("http://searchtip.kugou.com/getSearchTip?MVTipCount=0&albumcount=0")
    fun getMusicSuggestion(@Query("keyword") keyWord: String, @Query("MusicTipCount") count: Int = 5): Observable<FeedBack<MutableList<SuggestionContainer>>>

    @GET("https://c.y.qq.com/splcloud/fcgi-bin/smartbox_new.fcg?is_xml=0&format=jsonp&g_tk=5381&jsonpCallback=SmartboxKeysCallbackmod_top_search8875&loginUin=0&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0")
    fun getSingerSuggestion(@Query("key") keyWord: String): Observable<ResponseBody>
}