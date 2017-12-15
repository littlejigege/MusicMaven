package com.qg.musicmaven.ui.cloudpage.twopages

import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobile.utils.inUiThread
import com.mobile.utils.showToast
import com.qg.musicmaven.App
import com.qg.musicmaven.R
import com.qg.musicmaven.modle.QiNiu
import com.qg.musicmaven.modle.bean.ServerAudio
import com.qg.musicmaven.modle.bean.Singer
import com.qg.musicmaven.ui.adapter.ServerAudioAdapter
import com.qg.musicmaven.ui.adapter.SingerAdapter
import com.qg.musicmaven.ui.cloudpage.singerpage.SingerActivity
import kotlinx.android.synthetic.main.frag_server_audio.*
import kotlinx.android.synthetic.main.frag_server_audio.view.*
import kotlin.concurrent.thread

/**
 * Created by jimiji on 2017/12/12.
 */
class ServerAudioFragment : Fragment(), SAContract.View {


    override fun loadMoreDone(list: MutableList<Singer>) {
        if (activity != null) {
            if (list.isEmpty()) showToast("没有更多了o(*￣▽￣*)o")
            reFreshLayoutServerAudio.finishLoadmore(1)
            adapter.data.addAll(list)
            adapter.notifyDataSetChanged()
        }

    }

    override fun reFreshDone(list: MutableList<Singer>) {
        if (activity != null) {
            reFreshLayoutServerAudio.finishRefresh(1)
            adapter = SingerAdapter(list, activity)
            rootView!!.recyclerViewServerAudio.adapter = adapter
            rootView!!.recyclerViewServerAudio.layoutManager = GridLayoutManager(activity, 2)
        }

    }

    private var rootView: View? = null
    private lateinit var adapter: SingerAdapter
    private val presenter = SAPresenter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.takeView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.frag_server_audio, container, false)
        rootView?.reFreshLayoutServerAudio?.setOnLoadmoreListener { presenter.loadMore() }
        rootView?.reFreshLayoutServerAudio?.setOnRefreshListener { presenter.reFresh() }
        presenter.reFresh()
        return rootView
    }

    override fun onDestroy() {
        presenter.dropView()
        super.onDestroy()
    }

}