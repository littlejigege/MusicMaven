package com.qg.musicmaven.ui.dreampage

import com.qg.musicmaven.base.BaseView
import com.qg.musicmaven.modle.bean.Wish

/**
 * Created by jimiji on 2017/12/14.
 */
interface DreamContract {
    interface View : BaseView {
        fun loadMoreDone(list: MutableList<Wish>)
        fun reFreshDone(list: MutableList<Wish>)

    }
}