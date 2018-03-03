package com.qg.musicmaven.ui.cloudpage.twopages

import com.mobile.utils.isConnected
import com.mobile.utils.showToast
import com.qg.musicmaven.App
import com.qg.musicmaven.base.AbsBasePresenter
import com.qg.musicmaven.modle.bean.FeedBack
import com.qg.musicmaven.modle.bean.ServerAudio
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by jimiji on 2017/12/14.
 */
class MAPresenter : AbsBasePresenter<MAContract.View>() {
    private val pageNext = AtomicInteger(2)
    fun loadMore() {
        if (!App.instance.hasUser()) {
            view?.onNotLogin()
            return
        }
        App.serverApi.getsonglist(App.instance.getUser()?.userId?.toLong() ?: 0, pageNext.getAndIncrement())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<FeedBack<MutableList<ServerAudio>>> {
                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                    }

                    override fun onSubscribe(d: Disposable?) {

                    }

                    override fun onComplete() {

                    }

                    override fun onNext(it: FeedBack<MutableList<ServerAudio>>) {
                        view?.loadMoreDone(it.data)
                    }
                })
    }

    fun reFresh() {
        if (!App.instance.hasUser()) {
            view?.onNotLogin()
            return
        }

        pageNext.set(2)//重置
        App.serverApi.getsonglist(App.instance.getUser()?.userId?.toLong() ?: 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<FeedBack<MutableList<ServerAudio>>> {
                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                    }

                    override fun onSubscribe(d: Disposable?) {

                    }

                    override fun onComplete() {

                    }

                    override fun onNext(it: FeedBack<MutableList<ServerAudio>>) {
                        view?.reFreshDone(it.data)
                    }
                })


    }
}