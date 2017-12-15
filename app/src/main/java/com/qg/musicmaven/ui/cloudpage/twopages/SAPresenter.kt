package com.qg.musicmaven.ui.cloudpage.twopages

import com.mobile.utils.isConnected
import com.mobile.utils.showToast
import com.qg.musicmaven.App
import com.qg.musicmaven.base.AbsBasePresenter
import com.qg.musicmaven.modle.bean.FeedBack
import com.qg.musicmaven.modle.bean.Singer
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.atomic.AtomicInteger


/**
 * Created by jimiji on 2017/12/14.
 */
class SAPresenter : AbsBasePresenter<SAContract.View>() {
    private val pageNext = AtomicInteger(2)
    fun loadMore() {

        App.serverApi.getSinger(pageNext.getAndIncrement())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (object : Observer<FeedBack<MutableList<Singer>>> {
                    override fun onError(e: Throwable?) {
                        println(e)
                    }

                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable?) {

                    }

                    override fun onNext(it: FeedBack<MutableList<Singer>>) {
                        view?.loadMoreDone(it.data)
                    }
                })
    }

    fun reFresh() {

        pageNext.set(2)//重置
        App.serverApi.getSinger()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<FeedBack<MutableList<Singer>>> {
                    override fun onError(e: Throwable?) {
                        println(e)
                    }

                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable?) {

                    }

                    override fun onNext(it: FeedBack<MutableList<Singer>>) {
                        view?.reFreshDone(it.data)
                    }
                })


    }

}