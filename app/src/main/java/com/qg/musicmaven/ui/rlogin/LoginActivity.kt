package com.qg.musicmaven.rlogin

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.transition.Explode
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.mobile.utils.toast

import com.qg.musicmaven.R
import com.qg.musicmaven.mainpage.TestMainActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() , View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val explode = Explode()
        explode.duration = 500

        window.exitTransition = explode
        window.enterTransition = explode

        fab.setOnClickListener(this)
        bt_go.setOnClickListener(this)

        fab.alpha = 1f

        openCamera.setOnClickListener {
            openCamera.animate().alpha(0f).setDuration(300).start()
            fab.animate().alpha(0f).setDuration(300).start()
            val i2 = Intent(this, RegisterActivity::class.java)
            i2.putExtra("login",1)
            startActivity(i2)

        }

        bt_go.setOnClickListener {
            val account = et_username.text.toString()
            val pass = et_password.text.toString()

            var isBlank = false

            if(account.isBlank()){
                et_username.requestFocus()
                isBlank=true
            }

            if(pass.isBlank()){
                et_password.requestFocus()
                isBlank=true

            }

            if(isBlank){
                toast("请填写完整信息")
            }else{
                //todo  lgoin
            }


        }

    }



    override fun onResume() {
        super.onResume()
        openCamera.alpha = 1f
        fab.alpha = 1f
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.fab -> {
                window.exitTransition = null
                window.enterTransition = null

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val options = ActivityOptions.makeSceneTransitionAnimation(this, fab, fab!!.transitionName)
                    startActivity(Intent(this, RegisterActivity::class.java), options.toBundle())
                } else {
                    startActivity(Intent(this, RegisterActivity::class.java))
                }
            }
            R.id.bt_go -> {

                val oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(this)
                val i2 = Intent(this, TestMainActivity::class.java)
                startActivity(i2, oc2.toBundle())
            }
        }
    }
}
