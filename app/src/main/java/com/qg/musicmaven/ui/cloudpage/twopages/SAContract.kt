package com.qg.musicmaven.ui.cloudpage.twopages

import com.qg.musicmaven.base.AbsBasePresenter
import com.qg.musicmaven.base.BasePresenter
import com.qg.musicmaven.base.BaseView
import com.qg.musicmaven.modle.bean.ServerAudio
import com.qg.musicmaven.modle.bean.Singer
import com.qg.musicmaven.modle.bean.VerifyResult

/**
 * Created by steve on 17-12-12.
 */
interface SAContract {

    interface View : BaseView {
        fun loadMoreDone(list: MutableList<Singer>)
        fun reFreshDone(list: MutableList<Singer>)
    }


}