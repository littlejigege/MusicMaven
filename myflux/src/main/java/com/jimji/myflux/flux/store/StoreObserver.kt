package com.jimji.myflux.flux.store

/**
 * Created by jimji on 2017/9/11.
 */
interface StoreObserver {
    fun onChange(actionType: String)
    fun onError(actionType: String)
}