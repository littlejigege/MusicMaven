package com.jimji.myflux.flux.store

import android.view.View
import com.jimji.myflux.flux.action.Action
import org.greenrobot.eventbus.EventBus

/**
* Created by jimji on 2017/9/8.
*/
abstract class Store: Observable() {
    abstract val TAG: String
    private val bus = EventBus.builder().build()

    fun register(view: View) = bus.register(view)

    fun unregister(view: View) = bus.unregister(view)

    fun emitStoreChange() = bus.post(changeEvent())

    abstract fun changeEvent(): StoreChangeEvent

    abstract fun onAction(action: Action)

    inner class StoreChangeEvent
}