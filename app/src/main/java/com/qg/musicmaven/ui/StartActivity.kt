package com.qg.musicmaven.ui

import android.animation.Animator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator


import com.jimji.preference.Preference
import com.mobile.utils.isEmail
import com.mobile.utils.md5
import com.qg.musicmaven.App
import com.qg.musicmaven.R
import com.qg.musicmaven.modle.FeedBack
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import kotlinx.android.synthetic.main.activity_start.*
import okhttp3.Response
import okhttp3.ResponseBody
import org.jetbrains.anko.Android
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import utils.inUiThread
import utils.showToast

class StartActivity : BaseActivity() {
    var session: String = ""

    enum class Status(val code: Int) {
        OK(1),
        USER_ERROR(110),
        ACCOUNT_NOT_EXIST(250),
        PASSWORD_ERROR(300),
        ACCOUNT_ALREADY_EXIST(400),
        EMAIL_COUNT_ERROR(450),
        EMAIL_ERROR(600)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        initView()
    }

    private fun initView() {
        QMUIStatusBarHelper.translucent(this)
        plus.onClick { toRegister() }
        loginButton.onClick {
            if (!loginUser.text.isEmpty() && !loginPassword.text.isEmpty() && loginUserInput.error == null && loginPasswordInput.error == null) {
                login()
            }
        }
        registerButton.onClick {
            register()
            if (!registerUser.text.isEmpty() && !registerPassword.text.isEmpty() && registerUserInput.error == null && registerPasswordInput.error == null && registerPasswordEnsure.text == registerPassword.text) {
                register()
            }
        }
        codeButton.onClick {
            if (codeButton.text.toString() == "获取验证码") {
                if (!registerUser.text.isEmpty() && registerUserInput.error == null) {
                    getCode()
                }
                startTimeCount()
            }
        }
        loginUser.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (!p0.isEmpty() && !p0.toString().isEmail) {
                    loginUserInput.error = "format error"
                } else {
                    loginUserInput.error = null
                }
            }
        })
        loginPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (!p0.isEmpty() && p0.length < 6) {
                    loginPasswordInput.error = "six at least"
                } else {
                    loginPasswordInput.error = null
                }
            }
        })
        registerUser.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (!p0.isEmpty() && !p0.toString().isEmail) {
                    registerUserInput.error = "format error"
                } else {
                    registerUserInput.error = null
                }
            }
        })
        registerPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (!p0.isEmpty() && p0.length < 6) {
                    registerPasswordInput.error = "six at least"
                } else {
                    registerPasswordInput.error = null
                }
            }
        })
        registerPasswordEnsure.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (!p0.isEmpty() && p0.length < 6) {
                    registerPasswordEnsureInput.error = "six at least"
                } else {
                    registerPasswordEnsureInput.error = null
                }
            }
        })
    }

    private fun login() {
        emptyView.show(true)
        App.serverApi.login(loginUser.text.toString(), loginPassword.text.toString().md5())
                .subscribeOn(IoScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<FeedBack<Int>?> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onComplete() {

                    }

                    override fun onError(e: Throwable) {
                        emptyView.hide()
                        showToast("出现错误")
                    }

                    override fun onNext(t: FeedBack<Int>) {
                        emptyView.hide()
                        when (t.status) {
                            Status.OK.code -> {
                                Preference.save("user") { "id" - t.data }
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
        App.serverApi.register(JsonMaker.make {
            objects {
                "session" - session
                "email" - registerUser.text.toString()
                "password" - registerPassword.text.toString().md5()
                "code" - code
            }
        })
                .subscribeOn(IoScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<FeedBack<Int>?> {
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
        App.serverApi.getCode(registerUser.text.toString()).enqueue(object : Callback<ResponseBody?> {
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
        registerCard.visibility = View.VISIBLE
        registerCard.alpha = 0F
        registerCard.animate().alpha(1F)
                .setDuration(500)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {

                    }

                    override fun onAnimationEnd(p0: Animator?) {

                    }

                    override fun onAnimationCancel(p0: Animator?) {

                    }

                    override fun onAnimationStart(p0: Animator?) {

                    }
                })
                .start()

        loginCard.alpha = 1F
        loginCard.animate().alpha(0F)
                .setDuration(500)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {

                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        loginCard.visibility = View.GONE
                    }

                    override fun onAnimationCancel(p0: Animator?) {

                    }

                    override fun onAnimationStart(p0: Animator?) {

                    }
                }).start()

        plus.animate().alpha(0F)
                .scaleY(0F)
                .scaleX(0F)
                .setDuration(200)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {

                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        plus.visibility = View.GONE
                    }

                    override fun onAnimationCancel(p0: Animator?) {

                    }

                    override fun onAnimationStart(p0: Animator?) {

                    }
                })
                .start()

    }

    private fun toLogin() {
        loginCard.visibility = View.VISIBLE
        loginCard.alpha = 0F
        loginCard.animate().alpha(1F)
                .setDuration(500)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {

                    }

                    override fun onAnimationEnd(p0: Animator?) {

                    }

                    override fun onAnimationCancel(p0: Animator?) {

                    }

                    override fun onAnimationStart(p0: Animator?) {

                    }
                })
                .start()
        registerCard.alpha = 1F
        registerCard.animate().alpha(0F)
                .setDuration(500)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {

                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        registerCard.visibility = View.GONE
                    }

                    override fun onAnimationCancel(p0: Animator?) {

                    }

                    override fun onAnimationStart(p0: Animator?) {

                    }
                }).start()
        plus.visibility = View.VISIBLE
        plus.animate().alpha(1F)
                .scaleY(1F)
                .scaleX(1F)
                .setDuration(200)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {

                    }

                    override fun onAnimationEnd(p0: Animator?) {

                    }

                    override fun onAnimationCancel(p0: Animator?) {

                    }

                    override fun onAnimationStart(p0: Animator?) {

                    }
                })
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
