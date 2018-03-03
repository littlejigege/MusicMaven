package com.qg.musicmaven.ui.cloudpage.singerpage

import com.qg.musicmaven.base.AbsBasePresenter
import com.qg.musicmaven.base.BasePresenter
import com.qg.musicmaven.base.BaseView
import com.qg.musicmaven.modle.bean.ServerAudio

/**
 * Created by jimiji on 2017/12/14.
 */
interface SingerContract {
    interface View : BaseView {
        fun onGetAudio(list: MutableList<ServerAudio>)
        fun loadMoreDone(list: MutableList<ServerAudio>)
    }

}