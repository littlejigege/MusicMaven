package com.qg.musicmaven.kugoupage

import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobile.utils.showToast
import com.qg.musicmaven.R
import com.qg.musicmaven.ui.mainpage.TestMainActivity
import com.qg.musicmaven.modle.bean.AudioInfo
import com.qg.musicmaven.ui.adapter.AudioAdapter
import kotlinx.android.synthetic.main.frag_kugou.view.*

/**
 * Created by jimji on 17-11-23.
 */
class KugouFragment : Fragment(), IView {


    companion object {
        val TAG = 0
    }

    fun setAudioList(keyWord: String, data: MutableList<AudioInfo>) {
        rootView?.refreshLayoutKugou?.finishRefresh(1)//这个库的作者有点奇怪，他一定要刷新动画在1秒以上才能取消，所以这里用了库里没检查延迟的方法去取消
        rootView?.refreshLayoutKugou?.isEnableRefresh = false
        presenter.keyWord = keyWord
        adapter.replaceData(data)
        adapter.notifyDataSetChanged()
    }

    private val presenter = KugouPresenter()
    private var rootView: View? = null
    private lateinit var adapter: AudioAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        presenter.takeView(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //无论如何，Tag一定要先改
        TestMainActivity.fragMentShowingTag = TAG
        //如果这个碎片是新的就引入布局，否则直接返回
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_kugou, container, false)
            rootView!!.recyclerViewKugou.layoutManager = LinearLayoutManager(activity)
            adapter = AudioAdapter(mutableListOf(), activity)
            rootView!!.recyclerViewKugou.adapter = adapter
            rootView?.refreshLayoutKugou?.isEnableRefresh = false
            rootView!!.refreshLayoutKugou.setOnLoadmoreListener {
                presenter.loadMore()
            }

        }
        return rootView
    }

    override fun onLoadMore(newData: MutableList<AudioInfo>) {
        rootView?.refreshLayoutKugou?.finishLoadmore(1)
        adapter.setMoreData(newData)
    }

    override fun onNoMore() {
        rootView?.refreshLayoutKugou?.finishLoadmore(1)
        showToast("已经到底了哦")
    }

    override fun showSearching() {
        rootView?.refreshLayoutKugou?.isEnableRefresh = true
        rootView?.refreshLayoutKugou?.autoRefresh(60)
    }

    override fun onDestroy() {
        presenter.dropView()
        super.onDestroy()
    }
}