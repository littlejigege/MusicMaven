package com.qg.musicmaven.modle.bean

/**
 * Created by jimiji on 2017/12/17.
 */
data class QQSinger(var name: String, var mid: String)

data class QQSingerCount(var count: Int, var itemlist: MutableList<QQSinger>)

data class QQSugesstion(var singer: QQSingerCount)