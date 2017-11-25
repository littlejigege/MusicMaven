package com.qg.musicmaven.mainpage

/**
 * Created by jimji on 17-11-23.
 */
class MainPagePresenter : MainPageContract.Presenter {
    private var view: MainPageContract.View? = null
    override fun dropView() {
        view = null
    }

    override fun takeView(view: MainPageContract.View) {
        this.view = view
    }

    override fun onKugouClick() {
        view?.openKugouPage()
    }

    override fun onCloudClick() {
        view?.openCloudPage()
    }

    override fun onDreamClick() {
        view?.openDreamPage()
    }

    override fun onSettingClick() {
        view?.openSettingPage()
    }
}