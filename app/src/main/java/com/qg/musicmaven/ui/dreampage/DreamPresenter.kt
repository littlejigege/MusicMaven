package com.qg.musicmaven.ui.dreampage

import android.app.Activity
import android.app.ProgressDialog
import com.mobile.utils.JsonMaker
import com.mobile.utils.Preference
import com.mobile.utils.isConnected
import com.mobile.utils.showToast
import com.qg.musicmaven.App
import com.qg.musicmaven.base.AbsBasePresenter
import com.qg.musicmaven.modle.QiNiu
import com.qg.musicmaven.modle.bean.FeedBack
import com.qg.musicmaven.modle.bean.Wish
import com.qg.musicmaven.widget.UpLoadProgressDialog
import com.qiniu.android.storage.UpCancellationSignal
import com.qiniu.android.storage.UpProgressHandler
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import java.net.URL
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by jimiji on 2017/12/14.
 */
class DreamPresenter : AbsBasePresenter<DreamContract.View>() {
    private val pageNext = AtomicInteger(2)
    fun loadMore() {
        if (!App.instance.hasUser()) {
            showToast("请先登陆")
            return
        }
        App.serverApi.getWishList(page = pageNext.getAndIncrement())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<FeedBack<MutableList<Wish>>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable?) {

                    }

                    override fun onNext(it: FeedBack<MutableList<Wish>>) {
                        view?.loadMoreDone(it.data)
                    }

                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                    }
                })
    }

    fun reFresh() {
        if (!App.instance.hasUser()) {
            showToast("请先登陆")
            return
        }
        pageNext.set(2)//重置
        App.serverApi.getWishList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<FeedBack<MutableList<Wish>>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable?) {

                    }

                    override fun onNext(it: FeedBack<MutableList<Wish>>) {
                        view?.reFreshDone(it.data)
                    }

                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                    }
                })


    }

    fun achieveDream(wish: Wish, songKey: String) {
        App.serverApi.achieveDream(RequestBody.create(MediaType.parse("application/json"), JsonMaker.make {
            objects {
                "wishId" - wish.wishId
                "songURL" - "${App.QIQUI_ADDRESS}/$songKey"
            }
        })).subscribeOn(Schedulers.io())
                .subscribe(object : Observer<FeedBack<Int>?> {
                    override fun onComplete() {

                    }

                    override fun onNext(value: FeedBack<Int>?) {

                    }

                    override fun onSubscribe(d: Disposable?) {

                    }

                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                    }
                })
    }


    fun uploadSong(filePath: String, wish: Wish) {
        val dialog = UpLoadProgressDialog(view as Activity)
        dialog.show()
        QiNiu.upLoad(filePath, UpProgressHandler { key, percent ->
            dialog.updateProgress((percent * 100).toInt())
            if (percent == 1.0) {
                achieveDream(wish, key)
            }
        }, UpCancellationSignal { dialog.isCanceled })
    }
}