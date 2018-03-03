package com.qg.musicmaven.dreampage

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobile.utils.JsonMaker
import com.mobile.utils.showToast
import com.qg.musicmaven.App
import com.qg.musicmaven.R
import com.qg.musicmaven.modle.bean.FeedBack
import com.qg.musicmaven.modle.bean.Wish
import com.qg.musicmaven.ui.dreampage.EditWishDialog
import com.qg.musicmaven.ui.dreampage.WishActivity
import com.qg.musicmaven.ui.mainpage.TestMainActivity
import com.qg.musicmaven.ui.rlogin.LoginActivity
import com.qg.musicmaven.widget.BottomMenu
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.frag_dream.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.jetbrains.anko.startActivity
import java.sql.SQLOutput

/**
 * Created by jimji on 17-11-23.
 */
class DreamFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View) {
        //未登录先登录
        if (!App.instance.hasUser()) {
            startActivity<LoginActivity>()
            return
        }
        when (v.id) {
            R.id.cardViewWish -> {
                EditWishDialog(activity) {
                    //提交愿望
                    postWish(it)
                }
            }
            else -> {
                startActivity<WishActivity>()
            }
        }
    }

    companion object {
        val TAG = 2
    }

    private lateinit var rootView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        TestMainActivity.fragMentShowingTag = TAG
        rootView = inflater.inflate(R.layout.frag_dream, container, false)
        rootView.cardViewWish.setOnClickListener(this)
        rootView.cardVIewAchieve.setOnClickListener(this)
        return rootView
    }

    fun postWish(wish: Wish) {
        App.serverApi.postWish(RequestBody.create(MediaType.parse("application/json"), JsonMaker.make {
            objects {
                "customerId" - App.instance.getUser()!!.userId
                "songName" - wish.songName
                "singerName" - wish.singerName
            }
        })).subscribeOn(Schedulers.io())
                .subscribe(object : Observer<FeedBack<Int>> {
                    override fun onNext(value: FeedBack<Int>) {
                        println("wish" + value.status)
                    }

                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                    }

                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable?) {

                    }
                })
    }
}