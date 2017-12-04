package com.qg.musicmaven.rlogin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.transition.Explode

import com.qg.musicmaven.R

class LoginSuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_success)

        val explode = Explode()
        explode.duration = 500
        window.exitTransition = explode
        window.enterTransition = explode
    }
}
