package com.qg.musicmaven.mainpage

import com.qg.musicmaven.base.AbsBasePresenter
import com.qg.musicmaven.ui.mainpage.MainPageContract

/**
 * Created by jimji on 17-11-23.
 */
class MainPagePresenter : AbsBasePresenter<MainPageContract.View>() {




    fun onCloudClick() {
        view?.openCloudPage()
    }

    fun onDreamClick() {
        view?.openDreamPage()
    }

    fun onSettingClick() {
        view?.openSettingPage()
    }

    fun requestNotice(){
        view?.onNoticeGet("还能说什么？66666666666666666666666666666666666666666666666")
    }
}