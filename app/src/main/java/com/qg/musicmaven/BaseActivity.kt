package com.qg.musicmaven

import android.annotation.SuppressLint
import android.os.Bundle
import com.mobile.utils.permission.PermissionActivity
import com.qg.musicmaven.R


/**
 * Created by jimji on 2017/9/9.
 */
@SuppressLint("Registered")
open class BaseActivity : PermissionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarcolor()
    }


    private fun setStatusBarcolor() {
        window.statusBarColor = resources.getColor(R.color.statusBar)
    }


}