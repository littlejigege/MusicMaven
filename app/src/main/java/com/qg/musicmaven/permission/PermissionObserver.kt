package com.qg.musicmaven.permission

/**
 * Created by jimji on 2017/9/12.
 */
interface PermissionObserver {
    fun onDinied(dinied: List<String>, requested: Int)
    fun onPass(passed: List<String>, requested: Int)
}