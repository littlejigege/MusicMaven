package com.qg.musicmaven.mainpage

import com.qg.musicmaven.BasePresenter

/**
 * Created by jimji on 17-11-23.
 */
class MainPagePresenter : BasePresenter<MainPageContract.View>() {


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