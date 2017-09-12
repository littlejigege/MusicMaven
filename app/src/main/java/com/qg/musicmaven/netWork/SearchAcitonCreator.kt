package com.qg.musicmaven.netWork

import com.qg.musicmaven.App
import com.qg.musicmaven.modle.AudioInfoContainer
import com.qg.musicmaven.modle.FeedBack
import com.qg.musicmaven.modle.SuggestionContainer
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler

/**
 * Created by jimji on 2017/9/11.
 */
class SearchAcitonCreator : ActionCreator() {
    var disposable: Disposable? = null
    fun cancelSuggestion() {
        if (disposable != null) {
            disposable!!.dispose()
            disposable = null
        }
    }

    fun search(keyWord: String) {
        App.kugouApi.getAudioInfoList(keyWord)
                .subscribeOn(IoScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<FeedBack<AudioInfoContainer>?> {
                    override fun onNext(t: FeedBack<AudioInfoContainer>) {
                        postChange(Action("search", t.data.list))
                    }

                    override fun onError(e: Throwable) {
                        postErr(ActionError("searchErr", e))
                    }

                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }
                })
    }

    fun getSuggestion(keyWord: String) {
        App.kugouApi.getSuggestion(keyWord)
                .subscribeOn(IoScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<FeedBack<MutableList<SuggestionContainer>>?> {
                    override fun onError(e: Throwable) {
                        postErr(ActionError("suggestionErr", e))
                    }

                    override fun onNext(t: FeedBack<MutableList<SuggestionContainer>>) {
                        postChange(Action("suggestion", t.data))
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