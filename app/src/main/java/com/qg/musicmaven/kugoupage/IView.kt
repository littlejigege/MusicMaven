package com.qg.musicmaven.kugoupage

import com.qg.musicmaven.modle.bean.AudioInfo

/**
 * Created by jimiji on 2017/12/3.
 */
interface IView {
    fun onLoadMore(newData: MutableList<AudioInfo>)
    fun onNoMore()
    fun showSearching()
}