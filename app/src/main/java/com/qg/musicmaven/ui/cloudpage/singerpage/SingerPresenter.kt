package com.qg.musicmaven.ui.cloudpage.singerpage

import com.mobile.utils.inUiThread
import com.mobile.utils.toast
import com.qg.musicmaven.App
import com.qg.musicmaven.base.AbsBasePresenter
import com.qg.musicmaven.modle.QiNiu
import com.qg.musicmaven.modle.bean.FeedBack
import com.qg.musicmaven.modle.bean.ServerAudio
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlin.concurrent.thread

/**
 * Created by jimiji on 2017/12/14.
 */
class SingerPresenter : AbsBasePresenter<SingerContract.View>() {
    fun getSingerAudio(name: String) {
        App.serverApi.getSingerSong(1, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<FeedBack<MutableList<ServerAudio>>> {
                    override fun onError(e: Throwable?) {

                    }

                    override fun onComplete() {

                    }

                    override fun onNext(it: FeedBack<MutableList<ServerAudio>>) {
                        view?.onGetAudio(it.data)
                    }

                    override fun onSubscribe(d: Disposable?) {

                    }
                })
    }

}
