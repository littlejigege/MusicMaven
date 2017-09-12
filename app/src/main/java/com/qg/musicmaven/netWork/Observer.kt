package com.qg.musicmaven.netWork

/**
 * Created by jimji on 2017/9/11.
 */
interface Observer {
    fun onChange(atc:Action)
    fun onError(e: ActionError)
}