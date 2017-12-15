package com.qg.musicmaven.modle.bean

/**
 * Created by jimiji on 2017/12/13.
 */
data class Wish(var wishId: Int, var customerId: Int = 0, var songName: String = "", var singerName: String = "", var albumName: String = "")
