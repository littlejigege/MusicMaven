package com.qg.musicmaven.ui.cloudpage.singerpage

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.transition.Transition
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mobile.utils.inUiThread
import com.mobile.utils.permission.Permission
import com.mobile.utils.permission.PermissionCompatActivity
import com.mobile.utils.showToast
import com.mobile.utils.systemDownload
import com.qg.musicmaven.App

import com.qg.musicmaven.R
import com.qg.musicmaven.base.BaseActivity
import com.qg.musicmaven.modle.QiNiu
import com.qg.musicmaven.modle.bean.ServerAudio
import com.qg.musicmaven.modle.bean.Singer
import com.qg.musicmaven.ui.adapter.ServerAudioAdapter
import com.qg.musicmaven.utils.SlideInItemAnimator

import com.qg.musicmaven.widget.BottomMenu
import kotlinx.android.synthetic.main.activity_singer.*
import kotlinx.android.synthetic.main.frag_server_audio.view.*
import java.io.File
import kotlin.concurrent.thread


class SingerActivity : PermissionCompatActivity(), SingerContract.View {


    private val presenter = SingerPresenter()
    private lateinit var adapter: ServerAudioAdapter
    override fun loadMoreDone(list: MutableList<ServerAudio>) {
        adapter.data.addAll(list)
        adapter.notifyDataSetChanged()
    }

    override fun onGetAudio(list: MutableList<ServerAudio>) {
        adapter = ServerAudioAdapter(list, this)
        recyclerViewSingerAudio.adapter = adapter
        adapter.onItemClick {
            val audio = it
            BottomMenu(this) {
                when (it) {
                    BottomMenu.PLAY -> {
                        App.instance.playAudio(audio.playUrl,audio.songName,audio.singerName,audio.imgUrl)
                    }
                    BottomMenu.STANDER -> {
                        Permission.STORAGE.doAfterGet(this) {
                            systemDownload(audio.playUrl, File(App.DOWNLOAD_PATH +
                                    "/${audio.playUrl.substring(audio.playUrl.lastIndexOf("/") + 1)}"))
                        }
                    }
                    BottomMenu.HQ -> {
                        showToast("不存在的")
                    }
                }
            }
        }
    }

    companion object {
        //通过歌手名启动这个活动
        fun withSinger(singer: Singer, imageView: View, activity: Activity) {
            val intent = Intent(App.instance, SingerActivity::class.java)
            intent.putExtra("singer", singer)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                    Pair(imageView, "singerPic"), Pair(imageView, "background"))
            activity.startActivity(intent, options.toBundle())
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singer)
        presenter.takeView(this)
        setSupportActionBar(toolbarSingerActivity)
        //设置可返回
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //设置标题
        if (intent != null) {
            collapsingToolbarLayout.title = (intent.getSerializableExtra("singer") as Singer).name
            loadImg((intent.getSerializableExtra("singer") as Singer).imgUrl, imageViewSingerOfSingerActivity)
            collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE)
            collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE)
        }
        window.statusBarColor = Color.TRANSPARENT
        recyclerViewSingerAudio.layoutManager = LinearLayoutManager(this)
        //获取歌单
        delayLoadSongs()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finishAfterTransition()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

    private fun loadImg(url: String, imageView: ImageView) {
        Glide.with(this)
                .load(url)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_singer_gray))
                .into(imageView)
    }

    override fun onDestroy() {
        presenter.dropView()
        super.onDestroy()
    }

    private fun delayLoadSongs() {
        window.sharedElementReturnTransition = window.sharedElementEnterTransition.clone()
        window.sharedElementEnterTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionEnd(transition: Transition?) {
                presenter.getSingerAudio(collapsingToolbarLayout.title.toString())
            }

            override fun onTransitionResume(transition: Transition?) {

            }

            override fun onTransitionPause(transition: Transition?) {

            }

            override fun onTransitionCancel(transition: Transition?) {

            }

            override fun onTransitionStart(transition: Transition?) {

            }
        })
    }

}
