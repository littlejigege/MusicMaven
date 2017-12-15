package com.qg.musicmaven.ui.cloudpage.twopages

import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobile.utils.showToast
import com.qg.musicmaven.R
import com.qg.musicmaven.modle.bean.ServerAudio
import com.qg.musicmaven.ui.adapter.ServerAudioAdapter
import kotlinx.android.synthetic.main.frag_my_audio.view.*
import kotlinx.android.synthetic.main.frag_server_audio.view.*

/**
 * Created by jimiji on 2017/12/12.
 */
class MyAudioFragment : Fragment(), MAContract.View {
    override fun reFreshDone(list: MutableList<ServerAudio>) {
        if (activity != null) {
            rootView?.reFreshLayoutMyAudio.finishRefresh(1)
            adapter = ServerAudioAdapter(list, activity)
            rootView?.recyclerViewMyAudio.adapter = adapter
            rootView?.recyclerViewMyAudio.layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun loadMoreDone(list: MutableList<ServerAudio>) {
        if (activity != null) {
            rootView?.reFreshLayoutMyAudio.finishLoadmore(1)
            if (list.isEmpty()) showToast("没有更多了(●ˇ∀ˇ●)")
            adapter.data.addAll(list)
            adapter.notifyDataSetChanged()
        }

    }

    private val presenter = MAPresenter()
    private lateinit var rootView: View
    private lateinit var adapter: ServerAudioAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        presenter.takeView(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.frag_my_audio, container, false)
        rootView?.reFreshLayoutMyAudio?.setOnLoadmoreListener { presenter.loadMore() }
        rootView?.reFreshLayoutMyAudio?.setOnRefreshListener { presenter.reFresh() }
        //presenter.reFresh()
        return rootView
    }
}