package com.qg.musicmaven.ui.searchpage

import com.qg.musicmaven.base.BaseView
import com.qg.musicmaven.modle.bean.Audio
import com.qg.musicmaven.modle.bean.ServerAudio

/**
 * Created by jimiji on 2017/12/15.
 */
interface SearchContract {
    interface View : BaseView {
        fun isKG(): Boolean//是否在KG中搜索
        fun searchKGDone(list: MutableList<Audio>)
        fun searchServerDone(list: MutableList<ServerAudio>)
        fun onSearch()
    }
}