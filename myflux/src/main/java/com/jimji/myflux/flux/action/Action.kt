package com.jimji.myflux.flux.action

import com.jimji.preference.Preference

/**
 * Created by jimji on 2017/9/8.
 */
open class Action private constructor(val type: String, val data: MutableMap<String, Any>) {
    companion object {
        fun with(type: String, makePairs: Preference.AnyPairs.() -> Unit): Builder {
            val bulider = Builder()
            bulider.type = type
            val pairs = Preference.AnyPairs()
            pairs.makePairs()
            bulider.data = pairs.map
            return bulider
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String): T = data[key] as T


    class Builder {
        lateinit var type: String
        lateinit var data: MutableMap<String, Any>

        fun build(): Action {
            return Action(type, data)
        }

    }

}
