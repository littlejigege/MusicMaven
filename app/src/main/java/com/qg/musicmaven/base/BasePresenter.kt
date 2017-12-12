package com.qg.musicmaven.base

/**
 * Created by steve on 17-12-12.
 */
interface BasePresenter<in V : BaseView> {
    fun takeView(view : V)
    fun dropView()
}