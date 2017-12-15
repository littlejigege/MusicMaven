package com.qg.musicmaven.cloudpage

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qg.musicmaven.R
import com.qg.musicmaven.ui.cloudpage.twopages.TwoPagesAdapter
import com.qg.musicmaven.ui.mainpage.TestMainActivity
import kotlinx.android.synthetic.main.frag_cloud.view.*

/**
 * Created by jimji on 17-11-23.
 */
class CloudFragment : Fragment() {
    companion object {
        val TAG = 1
    }

    private lateinit var rootView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        TestMainActivity.fragMentShowingTag = TAG
        rootView = inflater.inflate(R.layout.frag_cloud, container, false)
        rootView.viewPager.adapter = TwoPagesAdapter(childFragmentManager)
        rootView.tabLayout.setupWithViewPager(rootView.viewPager)
        return rootView
    }
}