package com.qg.musicmaven.ui.searchpage

import com.mobile.utils.JsonMaker
import com.mobile.utils.systemDownload
import com.mobile.utils.value
import com.qg.musicmaven.App
import com.qg.musicmaven.base.AbsBasePresenter
import com.qg.musicmaven.modle.bean.Audio
import com.qg.musicmaven.modle.bean.AudioInfo
import com.qg.musicmaven.modle.bean.Singer
import com.qg.musicmaven.utils.Fetcher
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.dialog_upload.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

/**
 * Created by jimiji on 2017/12/17.
 */
class SearchPresenter : AbsBasePresenter<SearchContract.View>() {
    private val pageNext = AtomicInteger(2)
    private var keyWord = ""
    private val fetcher = Fetcher()
    fun search(keyWord: String) {
        this.keyWord = keyWord
        pageNext.set(2)
        if (view.isKG()) {
            fetcher.fetchIO(App.kugouApi.getAudioInfoList(keyWord), onNext = { feedBack ->
                view?.searchKGDone(feedBack.data.list)
            })
        } else {
            fetcher.fetchIO(App.serverApi.searchSong(keyWord), onNext = {
                view?.searchServerDone(it.data)
            })
        }
    }


    fun play(url: String, songName: String, singer: String, imageUrl: String) {
        App.instance.playAudio(url, songName, singer, imageUrl)
    }

    fun loadMore() {
        if (view.isKG()) {
            fetcher.fetchIO(App.kugouApi.getAudioInfoList(keyWord, pageNext.getAndIncrement()), onNext = { feedBack ->
                view?.loadMoreKGDone(feedBack.data.list)
            }, onError = { throwable ->
                throwable.printStackTrace()
                view?.loadMoreKGDone(mutableListOf())
            })
        } else {
            fetcher.fetchIO(App.serverApi.searchSong(keyWord, pageNext.getAndIncrement())
                    , onNext = {
                view?.loadMoreServerDone(it.data)
            })
        }
    }

    fun getAudio(info: AudioInfo) {
        fetcher.fetchIO(App.kugouApi.getAudio(info.fileHash, info.albumId), onNext = { feedBack ->
            view?.onAudioGet(feedBack.data)
        })
    }

    fun getSuggestion(keyWord: String) {
        fetcher.fetchIO(App.kugouApi.getMusicSuggestion(keyWord), onNext = { feedBack ->
            view?.onSuggestionGet(feedBack.data[0].suggestions.map { it.info }.toMutableList())
            return@fetchIO
        })
    }

    fun tellServerIDownloadSongFromKG(audio: Audio) {
        fetcher.fetchIO(App.serverApi.postKGSong(RequestBody.create(MediaType.parse("application/json"), JsonMaker.make {
            objects {
                "playUrl" - audio.playUrl
                "songName" - audio.audioName.split(" - ")[1]
                "singerName" - audio.audioName.split(" - ")[0]
                "customerId" - if (App.instance.hasUser()) App.instance.getUser()!!.userId else "-1"
                "imgUrl" - audio.imgUrl
            }
        })))
    }
}