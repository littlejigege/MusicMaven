package com.qg.musicmaven.modle

import com.qg.musicmaven.App
import com.qg.musicmaven.modle.bean.AudioInfo
import com.qg.musicmaven.modle.bean.AudioInfoContainer
import com.qg.musicmaven.modle.bean.FeedBack
import com.qg.musicmaven.modle.bean.SuggestionContainer
import com.qg.musicmaven.netWork.Action
import com.qg.musicmaven.netWork.ActionCreator
import com.qg.musicmaven.netWork.ActionError
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler

/**
 * Created by jimji on 2017/9/11.
 */
object SearchAcitonCreator : ActionCreator() {
    var disposable: Disposable? = null
    fun cancelSuggestion() {
        if (disposable != null) {
            disposable!!.dispose()
            disposable = null

        }
    }

    fun searchFromKugou(keyWord: String, page: Int, action: (MutableList<AudioInfo>) -> Unit) {
        App.kugouApi.getAudioInfoList(keyWord, page)
                .subscribeOn(IoScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<FeedBack<AudioInfoContainer>> {
                    override fun onNext(t: FeedBack<AudioInfoContainer>) {
                        action(t.data.list)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }
                })
    }

    fun getSuggestionFromKugou(keyWord: String, action: (MutableList<String>) -> Unit) {
        App.kugouApi.getSuggestion(keyWord)
                .subscribeOn(IoScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<FeedBack<MutableList<SuggestionContainer>>> {
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                    override fun onNext(t: FeedBack<MutableList<SuggestionContainer>>) {
                        val list: MutableList<String> = mutableListOf()
                        for (container in t.data) {
                            container.suggestions.forEach { list.add(it.info) }
                        }
                        action(list)
                    }

                    override fun onComplete() {
                        disposable = null
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }
                })
    }
}