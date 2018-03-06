package com.qg.musicmaven.mainpage

import com.qg.musicmaven.App
import com.qg.musicmaven.BuildConfig
import com.qg.musicmaven.base.AbsBasePresenter
import com.qg.musicmaven.modle.Status
import com.qg.musicmaven.modle.bean.UpdateInfo
import com.qg.musicmaven.ui.mainpage.MainPageContract
import com.qg.musicmaven.utils.Fetcher

/**
 * Created by jimji on 17-11-23.
 */
class MainPagePresenter : AbsBasePresenter<MainPageContract.View>() {


    val fetcher: Fetcher = Fetcher()

    fun onCloudClick() {
        view?.openCloudPage()
    }

    fun onDreamClick() {
        view?.openDreamPage()
    }

    fun onSettingClick() {
        view?.openSettingPage()
    }

    fun requestNotice() {
        fetcher.fetchIO(App.serverApi.getNotice(), onNext = {
            view?.onNoticeGet(it.data.noticeMessage)
        })
    }

    fun checkForUpdate() {
        fetcher.fetchIO(App.serverApi.checkApk(), onNext = {
            if (it.status == Status.OK.code) {
                view?.onUpdateGet(it.data)
            }
        })
    }
}