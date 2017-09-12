package com.qg.musicmaven.ui

import android.content.res.TypedArray
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.textservice.SuggestionsInfo
import android.widget.TextView
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.qg.musicmaven.R
import com.qg.musicmaven.modle.AudioInfo
import com.qg.musicmaven.modle.SuggestionContainer
import com.qg.musicmaven.netWork.Action
import com.qg.musicmaven.netWork.ActionError
import com.qg.musicmaven.netWork.SearchAcitonCreator
import com.qg.musicmaven.ui.adapter.AudioAdapter
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet

import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import utils.showToast


class MainActivity : BaseActivity(), MaterialSearchView.OnQueryTextListener, com.qg.musicmaven.netWork.Observer {
    lateinit var adapter: AudioAdapter
    var keyWord = ""
    private val actionCreator = SearchAcitonCreator()
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
        //FilePicker(this, fragmentManager).show()
        initView()
    }


    private fun initView() {
        setActionBar(toolbar)
        emptyView.show("什么都没有，快去搜索吧", "输入歌名或者歌手名进行搜素")
        animateToolbar()
        adapter = AudioAdapter(mutableListOf(), this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.onItemClick { showBottomSheet() }
        scrim.onClick {
            if (searchView.isSearchOpen) {
                searchView.closeSearch()
                scrim.visibility = View.GONE
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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

    private fun showBottomSheet() {
        QMUIBottomSheet.BottomListSheetBuilder(this)
                .addItem(R.drawable.ic_stander, "标准音质", "")
                .addItem(R.drawable.ic_hq, "高品音质", "")
                .setOnSheetItemClickListener { dialog, _, pos, _ ->
                    dialog.dismiss()
                    when (pos) {
                        0 -> {
                            showToast("开始下载标准音质")
                        }
                        1 -> {
                            showToast("开始下载高品音质")
                        }
                    }
                }
                .setTitle("下载")
                .build()
                .show()
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
                array.toTypedArray().map { Log.d("asd",it) }
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
