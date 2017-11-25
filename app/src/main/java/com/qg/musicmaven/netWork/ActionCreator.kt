package com.qg.musicmaven.netWork

/**
 * Created by jimji on 2017/9/11.
 */
open class ActionCreator {
    var observers = mutableListOf<com.qg.musicmaven.netWork.Observer>()
    fun register(a: com.qg.musicmaven.netWork.Observer) = observers.add(a)
    fun unregister(a: com.qg.musicmaven.netWork.Observer) = observers.remove(a)
    fun postErr(e: ActionError) = observers.map { it.onError(e) }
    fun postChange(atc: Action) = observers.map { it.onChange(atc) }

}