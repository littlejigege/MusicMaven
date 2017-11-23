package com.qg.musicmaven.ui

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.mobile.utils.*
import com.qg.musicmaven.App
import com.qg.musicmaven.BuildConfig
import com.qg.musicmaven.R
import com.qg.musicmaven.modle.FeedBack
import com.qg.musicmaven.modle.Status
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import kotlinx.android.synthetic.main.activity_start.*
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.jetbrains.anko.sdk25.coroutines.onClick
import retrofit2.Call
import retrofit2.Callback

class StartActivity : BaseActivity() {
    var session: String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        initView()
    }

    private fun initView() {
        QMUIStatusBarHelper.translucent(this)
        plus.onClick { toRegister() }
        loginButton.onClick(3000) {
            //用户名不对
            if (loginUser.text.isEmpty() || loginUserInput.error != null) return@onClick
            //密码不对
            if (loginPassword.text.isEmpty() || loginPasswordInput.error != null) return@onClick
            login()
        }
        registerButton.onClick(3000) {
            //用户名不对
            if (registerUser.value.isEmpty() || registerUserInput.error != null) return@onClick
            //密码不对
            if (registerPassword.value.isEmpty() || registerPasswordInput.error != null) return@onClick
            //确认密码不对
            if (registerPassword.value != registerPasswordEnsure.value) return@onClick
            //验证码为空
            if (code.value.isEmpty()) return@onClick
            register()
        }
        codeButton.onClick {
            if (codeButton.text.toString() == "获取验证码") {
                if (!registerUser.text.isEmpty() && registerUserInput.error == null) {
                    getCode()
                }
                startTimeCount()
            }
        }
        loginUser.onTextChange { p0 ->
            if (!p0.isEmpty() && !p0.isEmail) {
                loginUserInput.error = "format error"
            } else {
                loginUserInput.error = null
            }
        }

        loginPassword.onTextChange { p0 ->
            if (!p0.isEmpty() && p0.length < 6) {
                loginPasswordInput.error = "six at least"
            } else {
                loginPasswordInput.error = null
            }
        }

        registerUser.onTextChange { p0 ->
            if (!p0.isEmpty() && !p0.isEmail) {
                registerUserInput.error = "format error"
            } else {
                registerUserInput.error = null
            }
        }

        registerPassword.onTextChange { p0 ->
            if (!p0.isEmpty() && p0.length < 6) {
                registerPasswordInput.error = "six at least"
            } else {
                registerPasswordInput.error = null
            }
        }

        registerPasswordEnsure.onTextChange { p0 ->
            if (p0 != registerPassword.value) {
                registerPasswordEnsureInput.error = "not the same"
            } else {
                registerPasswordEnsureInput.error = null
            }
        }
    }

    private fun login() {
        inUiThread { emptyView.show(true) }
        App.serverApi.login(RequestBody.create(MediaType.parse("application/json"),JsonMaker.make {
            objects {
                "userEmail" - loginUser.value
                "password" - loginPassword.value.md5()
            }
        }))
                .subscribeOn(IoScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<FeedBack<Long>> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onComplete() {

                    }

                    override fun onError(e: Throwable) {
                        emptyView.hide()
                        showToast("出现错误")
                    }

                    override fun onNext(t: FeedBack<Long>) {
                        emptyView.hide()
                        when (t.status) {
                            Status.OK.code -> {
                                Preference.save("user") { "userId" - t.data }
                                finish()
                            }
                            Status.USER_ERROR.code -> {
                                showToast("有空值")
                            }
                            Status.ACCOUNT_NOT_EXIST.code -> {
                                showToast("账户不存在")
                            }
                            Status.PASSWORD_ERROR.code -> {
                                showToast("密码错误")
                            }
                            Status.EMAIL_COUNT_ERROR.code -> {
                                showToast("邮箱格式不正确")
                            }
                        }
                    }
                })
    }

    private fun register() {
        App.serverApi.register(RequestBody.create(MediaType.parse("application/json"), JsonMaker.make {
            objects {
                "userEmail" - registerUser.text.toString()
                "password" - registerPassword.text.toString().md5()
                "registerCount" - code.value
            }
        }))
                .subscribeOn(IoScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<FeedBack<Int>> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        showToast("出现错误")
                    }

                    override fun onComplete() {

                    }

                    override fun onNext(t: FeedBack<Int>) {
                        when (t.status) {
                            Status.OK.code -> {
                                toLogin()
                                showToast("注册成功")
                            }
                            Status.USER_ERROR.code -> {
                                showToast("有空值")
                            }
                            Status.ACCOUNT_ALREADY_EXIST.code -> {
                                showToast("账户已存在")
                            }
                            Status.EMAIL_COUNT_ERROR.code -> {
                                showToast("邮箱格式不正确")
                            }
                            Status.EMAIL_ERROR.code -> {
                                showToast("邮箱改了？前后不一致哦")
                            }
                            Status.AUTH_NULL.code -> {
                                showToast("验证码有误")
                            }
                        }
                    }
                })
    }

    private fun startTimeCount() {
        Thread({
            for (i in 60 downTo 1) {
                inUiThread { codeButton.text = "${i}秒后重试" }
                Thread.sleep(1000)
            }
            inUiThread { codeButton.text = "获取验证码" }
        }).start()
    }

    private fun getCode() {
        App.serverApi.getCode(registerUser.value).enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>?, response: retrofit2.Response<ResponseBody?>) {
                val tmp = response.headers().get("Set-Cookie")!!
                session = tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(";"))

            }

            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
                t?.printStackTrace()
            }
        })


    }


    private fun toRegister() {
        registerCard.visiable()
        registerCard.alpha = 0F
        registerCard.animate().alpha(1F)
                .setDuration(500)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .afterDone { }
                .start()

        loginCard.alpha = 1F
        loginCard.animate().alpha(0F)
                .setDuration(500)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .afterDone { }.start()

        plus.animate().alpha(0F)
                .scaleY(0F)
                .scaleX(0F)
                .setDuration(200)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .afterDone { plus.gone() }
                .start()

    }

    private fun toLogin() {
        loginCard.visiable()
        loginCard.alpha = 0F
        loginCard.animate().alpha(1F)
                .setDuration(500)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .afterDone { }
                .start()
        registerCard.alpha = 1F
        registerCard.animate().alpha(0F)
                .setDuration(500)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .afterDone { registerCard.gone() }.start()
        plus.visiable()
        plus.animate().alpha(1F)
                .scaleY(1F)
                .scaleX(1F)
                .setDuration(200)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .afterDone { }
                .start()
    }

    override fun onBackPressed() {
        if (plus.visibility == View.GONE) {
            toLogin()
        } else {
            super.onBackPressed()
        }
    }
}
