package com.qg.musicmaven.ui.cloudpage.singerpage

import com.mobile.utils.inUiThread
import com.mobile.utils.toast
import com.qg.musicmaven.App
import com.qg.musicmaven.base.AbsBasePresenter
import com.qg.musicmaven.modle.QiNiu
import com.qg.musicmaven.modle.bean.FeedBack
import com.qg.musicmaven.modle.bean.ServerAudio
import com.qg.musicmaven.utils.Fetcher
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

/**
 * Created by jimiji on 2017/12/14.
 */
class SingerPresenter : AbsBasePresenter<SingerContract.View>() {
    private val pageNext = AtomicInteger(2)
    private val fetcher = Fetcher()
    fun getSingerAudio(name: String) {
        fetcher.fetchIO(App.serverApi.getSingerSong(name), onNext = { view?.onGetAudio(it.data) })
    }

    fun loadMore(name: String) {
        fetcher.fetchIO(App.serverApi.getSingerSong(name, pageNext.getAndIncrement()), onNext = { view?.onGetAudio(it.data) })
    }
}
