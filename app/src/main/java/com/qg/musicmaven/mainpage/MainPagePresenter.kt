package com.qg.musicmaven.mainpage

import com.qg.musicmaven.base.AbsBasePresenter

/**
 * Created by jimji on 17-11-23.
 */
class MainPagePresenter : AbsBasePresenter<MainPageContract.View>() {


    fun onKugouClick() {
        view?.openKugouPage()
    }

    fun onCloudClick() {
        view?.openCloudPage()
    }

    fun onDreamClick() {
        view?.openDreamPage()
    }

    fun onSettingClick() {
        view?.openSettingPage()
    }
}