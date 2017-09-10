package com.jimji.myflux.flux.dispatcher

import com.jimji.myflux.flux.action.Action
import com.jimji.myflux.flux.store.Store


/**
* Created by jimji on 2017/9/8.
*/
class Dispatcher {
    companion object {
        val instance = Dispatcher()
    }

    private val stores by lazy { mutableListOf<Store>() }
    /**
     * store注册
     */
    fun register(store: Store) = stores.add(store)

    /**
     * store注销
     */
    fun unregister(store: Store) = stores.remove(store)

    /**
     * store按tag注销
     */
    fun unregister(tag: String) = stores.filter { tag == it.TAG }.forEach { stores.remove(it) }

    /**
     * action事件分发
     */
    fun dispatch(action: Action) = post(action)


    private fun post(action: Action) {
        stores.map { store -> store.onAction(action) }
    }
}