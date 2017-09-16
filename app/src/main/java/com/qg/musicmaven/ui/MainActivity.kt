package com.qg.musicmaven.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import com.jimji.preference.Preference
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.qg.musicmaven.App
import com.qg.musicmaven.R
import com.qg.musicmaven.download.DownloadCallback
import com.qg.musicmaven.modle.AudioInfo
import com.qg.musicmaven.modle.FeedBack
import com.qg.musicmaven.modle.ServerAudio
import com.qg.musicmaven.modle.SuggestionContainer
import com.qg.musicmaven.netWork.Action
import com.qg.musicmaven.netWork.ActionError
import com.qg.musicmaven.netWork.DownLoadActionCreator
import com.qg.musicmaven.netWork.SearchAcitonCreator
import com.qg.musicmaven.ui.adapter.AudioAdapter
import com.qg.musicmaven.ui.adapter.ServerAudioAdapter
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler


import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.downloadManager
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import utils.showToast

class MainActivity : BaseActivity(), MaterialSearchView.OnQueryTextListener, com.qg.musicmaven.netWork.Observer {

    lateinit var adapter: AudioAdapter
    var keyWord = ""
    private val actionCreator = SearchAcitonCreator()
    private val downloadCreator = DownLoadActionCreator(this)
    override fun onQueryTextSubmit(query: String): Boolean {
        keyWord = query
        emptyView.show(true)
        actionCreator.search(query)
        searchView.closeSearch()
        return true

    }

    override fun onQueryTextChange(newText: String): Boolean {
        if (!newText.isEmpty()) {
            actionCreator.cancelSuggestion()
            actionCreator.getSuggestion(newText)
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        actionCreator.register(this)
        initView()
    }


    private fun initView() {
        setActionBar(toolbar)
        emptyView.show("什么都没有，快去搜索吧", "输入歌名或者歌手名进行搜素")
        animateToolbar()
        adapter = AudioAdapter(mutableListOf(), this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.onItemClick {
            permissionMan.doAfterGet(permissionMan.STORAGE) {
                showBottomSheet(it)
            }
        }
        scrim.onClick {
            if (searchView.isSearchOpen) {
                searchView.closeSearch()
                scrim.visibility = View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        searchView.setMenuItem(menu!!.findItem(R.id.action_search))
        searchView.setOnQueryTextListener(this)
        searchView.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewClosed() {
                scrim.visibility = View.GONE
            }

            override fun onSearchViewShown() {
                scrim.visibility = View.VISIBLE
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {

            }
            R.id.action_setting -> {
                startActivity<SettingsActivity>()
            }
            R.id.action_cloud -> {
                val id = Preference.get("user", "id" to -1L) as Long
                if (id == -1L) {
                    startActivity<StartActivity>()
                } else {
                    emptyView.show(true)
                    App.serverApi.getsonglist(id)
                            .subscribeOn(IoScheduler())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : Observer<FeedBack<MutableList<ServerAudio>>?> {
                                override fun onComplete() {

                                }

                                override fun onNext(t: FeedBack<MutableList<ServerAudio>>) {
                                    emptyView.hide()
                                    recyclerView.adapter = ServerAudioAdapter(t.data, this@MainActivity)
                                }

                                override fun onSubscribe(d: Disposable) {

                                }

                                override fun onError(e: Throwable) {
                                    emptyView.show("抱歉，有点问题", "再试一次吧")
                                }
                            })
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun animateToolbar() {
        val t = toolbar.getChildAt(0)
        if (t != null && t is TextView) {
            val title = t
            title.alpha = 0f
            title.scaleX = 0.8f

            title.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .setStartDelay(300)
                    .setDuration(900).interpolator = AccelerateDecelerateInterpolator()
        }
    }

    override fun onBackPressed() {
        if (searchView.isSearchOpen) {
            searchView.closeSearch()
            scrim.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }

    override fun onStop() {
        actionCreator.unregister(this)
        super.onStop()
    }

    private fun showBottomSheet(item: AudioInfo) {
        QMUIBottomSheet.BottomListSheetBuilder(this)
                .addItem(R.drawable.ic_stander, "标准音质", "")
                .addItem(R.drawable.ic_hq, "高品音质", "")
                .setOnSheetItemClickListener { dialog, _, pos, _ ->
                    dialog.dismiss()
                    when (pos) {
                        0 -> {
                            showToast("开始下载标准音质")
                            downloadCreator.download(item, false, object : DownloadCallback {
                                override fun onPaused() {
                                    Log.d("asd", "onPaused")
                                }

                                override fun onPending() {
                                    Log.d("asd", "onPending")
                                }

                                override fun onRunning() {
                                    Log.d("asd", "onRunning")
                                }

                                override fun onSuccessful() {
                                    Log.d("asd", "onSuccessful")
                                }

                                override fun onFailed() {
                                    Log.d("asd", "onFailed")
                                }
                            })
                        }
                        1 -> {
                            showToast("开始下载高品音质")
                            downloadCreator.download(item, true, object : DownloadCallback {
                                override fun onPaused() {
                                    Log.d("asd", "onPaused")
                                }

                                override fun onPending() {
                                    Log.d("asd", "onPending")
                                }

                                override fun onRunning() {
                                    Log.d("asd", "onRunning")
                                }

                                override fun onSuccessful() {
                                    Log.d("asd", "onSuccessful")
                                }

                                override fun onFailed() {
                                    Log.d("asd", "onFailed")
                                }
                            })
                        }
                    }
                }
                .setTitle("下载")
                .build()
                .show()
    }

    override fun onResume() {
        super.onResume()
        //downloadCreator.checkApk()
    }

    override fun onChange(atc: Action) {
        when (atc.type) {
            "search" -> {
                emptyView.hide()
                adapter.data.clear()
                adapter.data.addAll(atc.data as MutableList<AudioInfo>)
                adapter.notifyDataSetChanged()
            }
            "suggestion" -> {
                showToast("in")
                val list = atc.data as MutableList<SuggestionContainer>
                val array = mutableListOf<String>()
                list.map { it.suggestions.map { array.add(it.info) } }
                array.toTypedArray().map { Log.d("asd", it) }
                searchView.setSuggestions(array.toTypedArray())
            }
        }

    }

    override fun onError(e: ActionError) {
        when (e.type) {
            "searchErr" -> {
                emptyView.show(false, "加载失败", null, "点击重试", {
                    emptyView.show(true)
                    actionCreator.search(keyWord)
                })
            }
            else -> {
                (e.data as Throwable).printStackTrace()
            }
        }
    }


}
