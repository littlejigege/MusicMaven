package com.qg.musicmaven.modle

import com.google.gson.annotations.SerializedName
import java.time.Duration

/**
 * Created by 小吉哥哥 on 2017/9/8.
 */
data class FeedBack<T>(var status: Int, @SerializedName("err_code") var errCode: Int, var data: T)

data class AudioInfoContainer(@SerializedName("lists")var list: MutableList<AudioInfo>)

data class AudioInfo(@SerializedName("SongName") var songName: String, @SerializedName("SingerName") var singerName: String,
                     @SerializedName("FileHash") var fileHash: String, @SerializedName("HQFileHash") var hqFileHash: String) {
    fun hasHq(): Boolean {
        //TODO
        return true
    }

    fun canDownload(): Boolean {
        //TODO
        return true
    }
}

data class Audio(@SerializedName("audio_name") var audioName: String, @SerializedName("img") var imgUrl: String,
                 @SerializedName("play_url") var playUrl: String, @SerializedName("filesize") var fileSize: Long,
                 @SerializedName("timelength") var duration: Long)