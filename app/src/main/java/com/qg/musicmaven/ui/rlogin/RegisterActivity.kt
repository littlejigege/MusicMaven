package com.qg.musicmaven.rlogin

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.transition.Transition
import android.transition.TransitionInflater
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import com.mobile.utils.md5
import com.mobile.utils.onClick
import com.mobile.utils.showToast
import com.qg.musicmaven.base.BaseActivity
import com.qg.musicmaven.R
import kotlinx.android.synthetic.main.activity_register.*
import com.mobile.utils.toggleVisibility
import com.qg.musicmaven.App
import com.qg.musicmaven.ui.mainpage.TestMainActivity
import com.qg.musicmaven.modle.bean.VerifyResult
import org.jetbrains.anko.custom.onUiThread
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.progressDialog
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast


class RegisterActivity : BaseActivity(), RegContract.View {

    var isLogin: Int = 0

    private var detecting = false

    override fun alreadyRegister(uuid: String) {
        toast("已注册过")
        App.uuid = uuid
        val oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(this)
        val i2 = Intent(this, TestMainActivity::class.java)
        startActivity(i2, oc2.toBundle())
        dialog?.dismiss()
        detecting = false
    }

    override fun registerSuccess() {
        toast("注册成功")
        dialog?.dismiss()
        detecting = false
    }

    override fun onError(e: Throwable) {
        showToast(e)
        dialog?.dismiss()
        detecting = false


    }

    val mPresenter: RegContract.Presenter by lazy {
        val presenter = RegPresenter()
        presenter.takeView(this)
        presenter
    }

    var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        isLogin = intent.getIntExtra("login", 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation()
        }
        if (isLogin == 1) {
            fab.visibility = View.GONE
            take.animate().x(cv_add.right.toFloat() + take.width).alpha(1f).setDuration(800).start()
            normal.visibility = View.GONE
        }

        fab!!.setOnClickListener { animateRevealClose() }
        bt_getCode.setOnClickListener {
            if (et_email.text.toString().isEmpty()) {
                et_email.requestFocus()
            } else {
                showToast("验证码已发送")
                mPresenter.getCode(et_email.text.toString())
            }
        }
        bt_go.setOnClickListener {
            val pass = et_password.text.toString()
            val rpass = et_repeatpassword.text.toString()
            val user = et_username.text.toString()
            val verify = count.text.toString()

            if (pass.isBlank()) et_password.requestFocus()
            if (rpass.isBlank()) et_repeatpassword.requestFocus()
            if (user.isBlank()) et_username.requestFocus()
            if (verify.isBlank()) count.requestFocus()
            if (pass.isNotBlank() && rpass.isNotBlank() && user.isNotBlank() && verify.isNotBlank()) {
                mPresenter.normalregister("2", et_email.text.toString(), pass.md5(), verify)
            }

        }


    }

    override fun onResume() {
        super.onResume()

    }

    private fun ShowEnterAnimation() {
        val transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition)
        window.sharedElementEnterTransition = transition

        transition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {
                cv_add!!.visibility = View.GONE
            }

            override fun onTransitionEnd(transition: Transition) {
                transition.removeListener(this)
                animateRevealShow()
            }

            override fun onTransitionCancel(transition: Transition) {

            }

            override fun onTransitionPause(transition: Transition) {

            }

            override fun onTransitionResume(transition: Transition) {

            }


        })
    }


    fun animateRevealShow() {
        val mAnimator = ViewAnimationUtils.createCircularReveal(cv_add, cv_add!!.width / 2, 0, (fab!!.width / 2).toFloat(), cv_add!!.height.toFloat())
        mAnimator.duration = 500
        mAnimator.interpolator = AccelerateInterpolator()
        mAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
            }

            override fun onAnimationStart(animation: Animator) {
                cv_add!!.visibility = View.VISIBLE
                take.animate().x(cv_add.right.toFloat() + take.width).alpha(1f).setDuration(800).start()
                super.onAnimationStart(animation)
            }
        })
        mAnimator.start()
    }

    fun animateRevealClose() {
        val mAnimator = ViewAnimationUtils.createCircularReveal(cv_add, cv_add!!.width / 2, 0, cv_add!!.height.toFloat(), (fab!!.width / 2).toFloat())
        mAnimator.duration = 500
        mAnimator.interpolator = AccelerateInterpolator()
        mAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                cv_add!!.visibility = View.INVISIBLE
                super.onAnimationEnd(animation)
                fab!!.setImageResource(R.drawable.plus)
                super@RegisterActivity.onBackPressed()
            }

            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
            }
        })
        mAnimator.start()
    }

    override fun onBackPressed() {
        if (isLogin != 1) {
            animateRevealClose()
        } else {
            super.onBackPressed()
        }
    }
}
