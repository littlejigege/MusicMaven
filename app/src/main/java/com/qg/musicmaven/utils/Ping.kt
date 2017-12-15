package com.qg.musicmaven.utils

import com.qg.musicmaven.App

/**
 * Created by jimiji on 2017/12/15.
 */
fun ping(address: String = App.SERVER_ADDRESS.split(":")[0]): Boolean {
    val process = Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 1 $address")
    return when (process.waitFor()) {
        0 -> {
            true
        }
        else -> {
            false
        }
    }
}