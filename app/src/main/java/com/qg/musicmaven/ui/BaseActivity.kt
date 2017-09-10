package com.qg.musicmaven.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.qg.musicmaven.R

/**
 * Created by jimji on 2017/9/9.
 */
@SuppressLint("Registered")
abstract class BaseActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarcolor()

    }


     private fun setStatusBarcolor() {
        window.statusBarColor = resources.getColor(R.color.statusBar)
    }

}