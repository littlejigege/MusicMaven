package com.qg.musicmaven.ui.cloudpage.twopages

import android.app.Fragment
import android.app.FragmentManager
import com.qg.musicmaven.ui.cloudpage.twopages.MyAudioFragment
import com.qg.musicmaven.ui.cloudpage.twopages.ServerAudioFragment

/**
 * Created by jimiji on 2017/12/12.
 */
class TwoPagesAdapter(fragmentManager: FragmentManager) : android.support.v13.app.FragmentPagerAdapter(fragmentManager) {
    private val serverAudioFrag by lazy { ServerAudioFragment() }
    private val myAudioFrag by lazy { MyAudioFragment() }
    override fun getItem(position: Int): Fragment = if (position == 0) serverAudioFrag else myAudioFrag

    override fun getCount() = 2
    override fun getPageTitle(position: Int) = if (position == 0) "服务器音乐区" else "我的音乐"


}