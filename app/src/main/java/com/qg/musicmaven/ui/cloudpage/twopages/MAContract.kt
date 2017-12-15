package com.qg.musicmaven.ui.cloudpage.twopages

import com.qg.musicmaven.base.BaseView
import com.qg.musicmaven.modle.bean.ServerAudio

/**
 * Created by jimiji on 2017/12/14.
 */
interface MAContract {
    interface View : BaseView {
        fun loadMoreDone(list: MutableList<ServerAudio>)
        fun reFreshDone(list: MutableList<ServerAudio>)
    }
}