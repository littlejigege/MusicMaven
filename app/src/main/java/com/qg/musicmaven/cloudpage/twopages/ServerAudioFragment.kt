package com.qg.musicmaven.cloudpage.twopages

import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobile.utils.inUiThread
import com.qg.musicmaven.App
import com.qg.musicmaven.R
import com.qg.musicmaven.modle.QiNiu
import com.qg.musicmaven.modle.bean.ServerAudio
import com.qg.musicmaven.ui.adapter.ServerAudioAdapter
import kotlinx.android.synthetic.main.frag_kugou.view.*
import kotlinx.android.synthetic.main.frag_server_audio.view.*
import kotlin.concurrent.thread

/**
 * Created by jimiji on 2017/12/12.
 */
class ServerAudioFragment : Fragment() {
    private lateinit var rootView: View
    private lateinit var list: MutableList<ServerAudio>
    private lateinit var adapter: ServerAudioAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.frag_server_audio, container, false)
        getServerAudio()
        return rootView
    }

    fun getServerAudio() {
        thread {
            list = QiNiu.getAllFile()
            inUiThread {
                adapter = ServerAudioAdapter(list, activity)
                adapter.onItemClick {
                    App.player.reset()
                    App.player.setDataSource(it.playUrl)
                    App.player.prepare()
                    App.player.start()
                }
                rootView.recyclerViewServerAudio.adapter = adapter
                rootView.recyclerViewServerAudio.layoutManager = LinearLayoutManager(activity)
            }
        }
    }
}