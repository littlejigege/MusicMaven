package com.jimji.myflux.flux.store

import java.util.Observable

/**
 * Created by jimji on 2017/9/11.
 */
open class Observable {
    private val mObserver = mutableListOf<StoreObserver>()
    fun addObserver(observer: StoreObserver) = mObserver.add(observer)
    fun notifyChange(actionType: String) = mObserver.map { it.onChange(actionType) }
    fun notifyError(actionType: String) = mObserver.map { it.onError(actionType) }
    fun clearObserver() = mObserver.clear()
    fun removeObserver(observer: StoreObserver) = mObserver.remove(observer)
}