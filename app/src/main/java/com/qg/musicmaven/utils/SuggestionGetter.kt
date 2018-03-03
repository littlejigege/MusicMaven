package com.qg.musicmaven.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.qg.musicmaven.App
import com.qg.musicmaven.modle.bean.FeedBack
import com.qg.musicmaven.modle.bean.QQSugesstion
import io.reactivex.disposables.Disposable

/**
 * Created by jimiji on 2017/12/17.
 */
object SuggestionGetter {
    private val fetcher = Fetcher()
    private var musicDisposable: Disposable? = null
    private var singerDisposable: Disposable? = null
    fun getMusicSuggestion(keyWord: String, count: Int = 5, action: (MutableList<String>) -> Unit) {
        if (musicDisposable != null) {
            musicDisposable!!.dispose()
        }
        fetcher.fetchIO(App.kugouApi.getMusicSuggestion(keyWord, count), onNext = {
            action(it.data[0].suggestions.map { it.info }.toMutableList())
        }, onSubscribe = { musicDisposable = it })
    }

    fun getSingerSuggestion(keyWord: String, action: (MutableList<String>) -> Unit) {
        if (singerDisposable != null) {
            singerDisposable!!.dispose()
        }
        fetcher.fetchIO(App.kugouApi.getSingerSuggestion(keyWord), onNext = {
            val string = it.string()
            val feedback = Gson().fromJson<FeedBack<QQSugesstion>>(string.substring(string.indexOf("(") + 1, string.lastIndexOf(")")), object : TypeToken<FeedBack<QQSugesstion>>(){}.type)
            action(feedback.data.singer.itemlist.map { it.name }.toMutableList())
        }, onSubscribe = { singerDisposable = it })
    }
}