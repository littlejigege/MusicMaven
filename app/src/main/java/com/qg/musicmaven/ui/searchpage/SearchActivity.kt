package com.qg.musicmaven.ui.searchpage

import android.database.Cursor
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.qg.musicmaven.R
import com.qg.musicmaven.base.BaseActivity
import kotlinx.android.synthetic.main.activity_search.*
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.SimpleCursorAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.SearchView
import android.widget.ArrayAdapter
import com.mobile.utils.permission.Permission
import com.mobile.utils.permission.PermissionCompatActivity
import com.mobile.utils.showToast
import com.mobile.utils.systemDownload
import com.qg.musicmaven.App
import com.qg.musicmaven.modle.bean.Audio
import com.qg.musicmaven.modle.bean.AudioInfo
import com.qg.musicmaven.modle.bean.ServerAudio
import com.qg.musicmaven.ui.adapter.AudioAdapter
import com.qg.musicmaven.ui.adapter.ServerAudioAdapter
import com.qg.musicmaven.utils.setData
import com.qg.musicmaven.widget.BottomMenu
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.find
import java.io.File


class SearchActivity : PermissionCompatActivity(), SearchContract.View {
    override fun onSuggestionGet(list: MutableList<String>) {
        searhView.setData(list.toTypedArray(), onSubmit = {
            presenter.search(it)
            false
        }, onTextChange = {
            if (it.isNotEmpty()) {
                presenter.getSuggestion(it)
            }
            true
        })
    }

    override fun loadMoreKGDone(list: MutableList<AudioInfo>) {
        refreshLayoutSearch.finishLoadmore(1)
        if (list.isEmpty()) {
            showToast("木有了( ఠൠఠ )ﾉ")
            return
        }
        audioAdapter?.data?.addAll(list)
        audioAdapter?.notifyDataSetChanged()
    }

    override fun loadMoreServerDone(list: MutableList<ServerAudio>) {
        refreshLayoutSearch.finishLoadmore(1)
        if (list.isEmpty()) {
            showToast("木有了( ఠൠఠ )ﾉ")
            return
        }
        serverAudioAdapter?.data?.addAll(list)
        serverAudioAdapter?.notifyDataSetChanged()
    }

    override fun onAudioGet(audio: Audio) {

        println("kg下载地址" + audio.playUrl)
        BottomMenu(this) {
            when (it) {
                BottomMenu.HQ -> {
                    showToast("不存在的哇咔咔")
                }
                BottomMenu.STANDER -> {
                    Permission.STORAGE.doAfterGet(this) {
                        presenter.tellServerIDownloadSongFromKG(audio)
                        systemDownload(audio.playUrl, File("${App.DOWNLOAD_PATH}/${audio.audioName}.mp3"))
                    }
                }
                BottomMenu.PLAY -> {
                    presenter.play(audio.playUrl,audio.audioName,"",audio.imgUrl)
                }
            }
        }
    }

    override fun isKG(): Boolean {
        return switchKG.isChecked
    }

    override fun searchKGDone(list: MutableList<AudioInfo>) {
        audioAdapter = AudioAdapter(list, this)
        audioAdapter?.onItemClick {
            presenter.getAudio(it)
        }
        recyclerViewSearch.adapter = audioAdapter
    }

    override fun searchServerDone(list: MutableList<ServerAudio>) {
        serverAudioAdapter = ServerAudioAdapter(list, this)
        serverAudioAdapter?.onItemClick {
            val audio = it
            BottomMenu(this) {
                when (it) {
                    BottomMenu.HQ -> {
                        showToast("不存在的哇咔咔")
                    }
                    BottomMenu.STANDER -> {
                        Permission.STORAGE.doAfterGet(this) {
                            systemDownload(audio.playUrl, File("${App.DOWNLOAD_PATH}/${audio.singerName}-${audio.songName}${audio.playUrl.substring(audio.playUrl.lastIndexOf("."))}"))
                        }
                    }
                    BottomMenu.PLAY -> {
                        presenter.play(audio.playUrl,audio.songName,audio.singerName,audio.imgUrl)
                    }
                }
            }
        }
        recyclerViewSearch.adapter = serverAudioAdapter
    }

    override fun onSearch() {

    }

    private lateinit var searhView: SearchView
    private var audioAdapter: AudioAdapter? = null
    private var serverAudioAdapter: ServerAudioAdapter? = null
    private val presenter = SearchPresenter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        refreshLayoutSearch.isEnableRefresh = false//不允许刷星
        refreshLayoutSearch.setOnLoadmoreListener { presenter.loadMore() }
        presenter.takeView(this)
        setSupportActionBar(toolbarActivitySearch)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerViewSearch.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        //拿到searchView
        searhView = MenuItemCompat.getActionView(menu.findItem(R.id.menu_search_search)) as SearchView
        //让他出来
        searhView.onActionViewExpanded()
        //添加提交按钮
        searhView.isSubmitButtonEnabled = true
        //修改背景颜色
        searhView.find<SearchView.SearchAutoComplete>(R.id.search_src_text).background = getDrawable(R.drawable.round_rect)
        //设置响应搜索
        searhView.setData(arrayOf(), onSubmit = {
            presenter.search(it)
            false
        }, onTextChange = {
            if (it.isNotEmpty()) {
                presenter.getSuggestion(it)
            }
            true
        })
//        searhView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                presenter.search(query)
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                if (newText.isNotEmpty()) {
//                    presenter.getSuggestion(newText)
//                }
//                return true
//            }
//        })

        return super.onCreateOptionsMenu(menu)
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

    override fun onDestroy() {
        presenter.dropView()
        super.onDestroy()
    }
}
