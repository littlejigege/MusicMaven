package com.qg.musicmaven.cloudpage.twopages

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qg.musicmaven.R

/**
 * Created by jimiji on 2017/12/12.
 */
class MyAudioFragment : Fragment() {
    private lateinit var rootView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.frag_my_audio, container, false)
        return rootView
    }
}