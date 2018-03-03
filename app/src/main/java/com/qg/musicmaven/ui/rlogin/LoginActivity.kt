package com.qg.musicmaven.ui.rlogin

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.transition.Explode
import android.view.View
import com.mobile.utils.JsonMaker
import com.mobile.utils.inUiThread
import com.mobile.utils.md5
import com.mobile.utils.permission.Permission
import com.mobile.utils.permission.PermissionCompatActivity
import com.mobile.utils.showToast
import com.qg.musicmaven.App

import com.qg.musicmaven.R
import com.qg.musicmaven.modle.bean.User
import com.qg.musicmaven.ui.mainpage.TestMainActivity
import com.qg.musicmaven.rlogin.RegisterActivity
import com.qg.musicmaven.utils.Fetcher
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.jetbrains.anko.toast

class LoginActivity : PermissionCompatActivity(), View.OnClickListener {

    val fetcher: Fetcher = Fetcher()
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

    }


    override fun onResume() {
        super.onResume()
        fab.alpha = 1f
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.fab -> {
                inUiThread {
                    window.exitTransition = null
                    window.enterTransition = null
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        val options = ActivityOptions.makeSceneTransitionAnimation(this, fab, fab!!.transitionName)
                        startActivity(Intent(this, RegisterActivity::class.java), options.toBundle())
                    } else {
                        startActivity(Intent(this, RegisterActivity::class.java))
                    }
                }


            }
            R.id.bt_go -> {
                val account = et_username.text.toString()
                val pass = et_password.text.toString()

                var isBlank = false

                if (account.isBlank()) {
                    et_username.requestFocus()
                    isBlank = true
                }

                if (pass.isBlank()) {
                    et_password.requestFocus()
                    isBlank = true

                }

                if (isBlank) {
                    toast("请填写完整信息")
                } else {
                    showToast("登陆中。。。")
                    fetcher.fetchIO(App.serverApi.login(RequestBody.create(MediaType.parse("application/json"), JsonMaker.make {
                        objects {
                            "userEmail" - account
                            "password" - pass.md5()
                        }

                    })), onNext = {
                        when (it.status) {
                            444 -> {
                                showToast("参数有误")
                            }
                            300 -> {
                                showToast("密码错误")
                            }
                            1 -> {
                                showToast("登陆成功")
                                loginSuccess(it.data)
                            }
                            250 ->{
                                showToast("账号不存在")
                            }
                            else -> {
                                showToast(it.status)
                            }
                        }

                    }, onError = { showToast(it) })

                }

            }
        }
    }

    private fun loginSuccess(user: User) {
        App.instance.saveUser(user)
        val oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(this)
        val i2 = Intent(this, TestMainActivity::class.java)
        startActivity(i2, oc2.toBundle())
        finishAfterTransition()
    }
}
