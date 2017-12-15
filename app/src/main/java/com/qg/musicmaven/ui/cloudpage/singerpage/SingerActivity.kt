package com.qg.musicmaven.ui.cloudpage.singerpage

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mobile.utils.inUiThread
import com.mobile.utils.permission.PermissionCompatActivity
import com.mobile.utils.showToast
import com.qg.musicmaven.App

import com.qg.musicmaven.R
import com.qg.musicmaven.base.BaseActivity
import com.qg.musicmaven.modle.QiNiu
import com.qg.musicmaven.modle.bean.ServerAudio
import com.qg.musicmaven.modle.bean.Singer
import com.qg.musicmaven.ui.adapter.ServerAudioAdapter
import com.qg.musicmaven.widget.BottomMenu
import kotlinx.android.synthetic.main.activity_singer.*
import kotlinx.android.synthetic.main.frag_server_audio.view.*
import kotlin.concurrent.thread


class SingerActivity : PermissionCompatActivity(), SingerContract.View {

    private val presenter = SingerPresenter()
    override fun onGetAudio(list: MutableList<ServerAudio>) {
        recyclerViewSingerAudio.adapter = ServerAudioAdapter(list, this)
        recyclerViewSingerAudio.layoutManager = LinearLayoutManager(this)
        (recyclerViewSingerAudio.adapter as ServerAudioAdapter).onItemClick {
            BottomMenu(this) {

            }
        }
    }

    companion object {
        //通过歌手名启动这个活动
        fun withSinger(singer: Singer) {
            val intent = Intent(App.instance, SingerActivity::class.java)
            intent.putExtra("singer", singer)
            App.instance.startActivity(intent)
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
        //获取歌单
        presenter.getSingerAudio(collapsingToolbarLayout.title.toString())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
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
}
