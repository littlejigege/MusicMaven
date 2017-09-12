package com.qg.musicmaven.netWork

import com.qg.musicmaven.modle.*
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * 存放酷狗相关的api
 * Created by 小吉哥哥 on 2017/9/7.
 */
interface KuGouApi {
    //关键字搜索jQuery112401895726466394836_1504849944559
    @GET("http://songsearch.kugou.com/song_search_v2?callback=&page=1&pagesize=30&userid=-1&clientver=&platform=WebFilter&tag=em&filter=2&iscorrection=1&privilege_filter=0&_=1504849944561")
    fun getAudioInfoList(@Query("keyword") keyWord: String): Observable<FeedBack<AudioInfoContainer>>

    //根据hash得到audio相关下载地址
    @GET("http://www.kugou.com/yy/index.php?r=play/getdata")
    fun getAudio(@Query("hash") hash: String): Observable<FeedBack<Audio>>

    //获得建议
    @GET("http://searchtip.kugou.com/getSearchTip?MusicTipCount=5&MVTipCount=2&albumcount=2")
    fun getSuggestion(@Query("keyword") keyWord: String): Observable<FeedBack<MutableList<SuggestionContainer>>>

    //文件下载
    @GET
    fun downloadAudio(@Url url: String): Observable<ResponseBody>
}