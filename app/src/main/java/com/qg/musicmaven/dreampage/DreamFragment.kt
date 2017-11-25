package com.qg.musicmaven.dreampage

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qg.musicmaven.R
import com.qg.musicmaven.mainpage.TestMainActivity

/**
 * Created by jimji on 17-11-23.
 */
class DreamFragment: Fragment() {
    companion object {
        val TAG = 2
    }
    private lateinit var rootView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        TestMainActivity.fragMentShowingTag = TAG
        rootView = inflater.inflate(R.layout.frag_dream, container,false)

        return rootView
    }
}