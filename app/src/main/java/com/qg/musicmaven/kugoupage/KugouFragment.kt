package com.qg.musicmaven.kugoupage

import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qg.musicmaven.R
import com.qg.musicmaven.mainpage.TestMainActivity
import com.qg.musicmaven.modle.bean.ServerAudio
import com.qg.musicmaven.ui.adapter.ServerAudioAdapter
import kotlinx.android.synthetic.main.frag_kugou.view.*

/**
 * Created by jimji on 17-11-23.
 */
class KugouFragment : Fragment() {
    companion object {
        val TAG = 0
    }
    private lateinit var rootView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        TestMainActivity.fragMentShowingTag = TAG
        rootView = inflater.inflate(R.layout.frag_kugou, container, false)
        rootView.recyclerViewKugou.layoutManager = LinearLayoutManager(activity)
        val data = mutableListOf<ServerAudio>()
        data.add(ServerAudio("", "", "asd", "34"))
        data.add(ServerAudio("", "", "asd", "34"))
        data.add(ServerAudio("", "", "asd", "34"))
        data.add(ServerAudio("", "", "asd", "34"))
        data.add(ServerAudio("", "", "asd", "34"))
        data.add(ServerAudio("", "", "asd", "34"))
        data.add(ServerAudio("", "", "asd", "34"))
        data.add(ServerAudio("", "", "asd", "34"))
        data.add(ServerAudio("", "", "asd", "34"))
        data.add(ServerAudio("", "", "asd", "34"))
        data.add(ServerAudio("", "", "asd", "34"))
        data.add(ServerAudio("", "", "asd", "34"))
        data.add(ServerAudio("", "", "asd", "34"))
        data.add(ServerAudio("", "", "asd", "34"))
        data.add(ServerAudio("", "", "asd", "34"))
        rootView.recyclerViewKugou.adapter = ServerAudioAdapter(data,activity)
        return rootView
    }
}