package com.qg.musicmaven.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.joker.annotation.PermissionsCustomRationale
import com.joker.annotation.PermissionsDenied
import com.joker.annotation.PermissionsGranted
import com.joker.annotation.PermissionsRequestSync
import com.joker.api.Permissions4M
import com.qg.musicmaven.R
import com.qg.musicmaven.permission.PermissionActivity

import com.qmuiteam.qmui.widget.dialog.QMUIDialog


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