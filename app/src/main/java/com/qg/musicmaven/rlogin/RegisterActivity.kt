package com.qg.musicmaven.rlogin

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.transition.Transition
import android.transition.TransitionInflater
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import com.iflytek.cloud.*
import com.mobile.utils.toBytes
import com.mobile.utils.toggle
import com.qg.musicmaven.base.BaseActivity

import com.qg.musicmaven.R
import kotlinx.android.synthetic.main.activity_register.*
import java.io.FileOutputStream
import com.iflytek.cloud.thirdparty.bm
import com.iflytek.speech.VerifierListener
import com.mobile.utils.toggleFile
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*


class RegisterActivity : BaseActivity() {
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation()
        }
        fab!!.setOnClickListener { animateRevealClose() }
        normal.setOnClickListener{
            animatedcancleCamera()
        }

        take.setOnClickListener {
            camera.takePhotoAnyWay()
        }


        addGroup("FUCKER008")

        val mIdVerifier = IdentityVerifier.createVerifier(this){ i -> Log.e("createCallback",i.toString())  }

        camera.onFaceDetected { bytes ->

            verify(bytes)
        }

    }

    fun verify(bytes: ByteArray){

        val mIdVerifier = IdentityVerifier.createVerifier(this){ i -> Log.e("createCallback",i.toString())  }
// 设置业务场景
        mIdVerifier.setParameter( SpeechConstant.MFV_SCENES, "ifr" );

// 设置业务类型：鉴别（identify）
        mIdVerifier.setParameter( SpeechConstant.MFV_SST, "identify" );


// 指定组id，最相似结果数
        val params = "group_id="+"3622740854"

// 设置监听器，开始会话
        mIdVerifier.startWorking( object : IdentityListener{
            override fun onResult(p0: IdentityResult?, p1: Boolean) {
                Log.e("RegisterActivity","${p0?.getResultString()}  ${p1.toString()}  查询}")
            }

            override fun onEvent(p0: Int, p1: Int, p2: Int, p3: Bundle?) {
                Log.e("RegisterActivity","event $p0  $p1  $p2 ")
            }

            override fun onError(p0: SpeechError?) {
                Log.e("RegisterActivity","Error  " + p0?.printStackTrace())
            }

        }  );


            // 写入数据
            mIdVerifier.writeData( "ifr", params, bytes, 0, bytes.size );


        mIdVerifier.stopWrite( "ifr" );
    }

    fun addToGroup(uuid:String){
        val mIdVerifier = IdentityVerifier.createVerifier(this){ i -> Log.e("createCallback",i.toString())  }

        mIdVerifier.setParameter( SpeechConstant.MFV_SCENES, "ipt" );

// 设置用户id
        mIdVerifier.setParameter( SpeechConstant.AUTH_ID, uuid );

// params 根据不同的操作而不同

        //创建组
        val params = "scope=person,group_id=" + "3622740854" +",auth_id=" + uuid;
        val cmd = "add";


// cmd 为 操作，模型管理包括 query, delete, download，组管理包括 add，query，delete
        mIdVerifier.execute( "ipt", cmd , params, object : IdentityListener{
            override fun onResult(p0: IdentityResult?, p1: Boolean) {
                Log.e("RegisterActivity","${p0?.getResultString()}  ${p1.toString()}")
            }

            override fun onEvent(p0: Int, p1: Int, p2: Int, p3: Bundle?) {
                Log.e("RegisterActivity","event $p0  $p1  $p2 ")
            }

            override fun onError(p0: SpeechError?) {
                Log.e("RegisterActivity","Error  Gup" + p0?.toString())
            }

        } );
    }

    fun addGroup(groupName:String){
        val mIdVerifier = IdentityVerifier.createVerifier(this){ i -> Log.e("createCallback",i.toString())  }

        mIdVerifier.setParameter( SpeechConstant.MFV_SCENES, "ipt" );

// 设置用户id
        mIdVerifier.setParameter( SpeechConstant.AUTH_ID, "system_user008" );

// params 根据不同的操作而不同

        //创建组
        val params="scope=group,group_name=" + groupName;
        val cmd = "add";


// cmd 为 操作，模型管理包括 query, delete, download，组管理包括 add，query，delete
        mIdVerifier.execute( "ipt", cmd , params, object : IdentityListener{
            override fun onResult(p0: IdentityResult?, p1: Boolean) {
                Log.e("RegisterActivity","${p0?.getResultString()}  ${p1.toString()} 成功查ungjianzuzu组 $groupName")
            }

            override fun onEvent(p0: Int, p1: Int, p2: Int, p3: Bundle?) {
                Log.e("RegisterActivity","event $p0  $p1  $p2 ")
            }

            override fun onError(p0: SpeechError?) {
                Log.e("RegisterActivity","Error  " + p0?.printStackTrace())
            }

        } );
    }

    fun register(bytes : ByteArray){

        val uuid = UUID.randomUUID().toString().replace("-","")
        Log.e("UUID",uuid)
        val mIdVerifier = IdentityVerifier.createVerifier(this){ i -> Log.e("RegisterActivity", "ASd")  }
        // 设置会话场景
        mIdVerifier.setParameter(SpeechConstant.MFV_SCENES, "ifr");
// 设置会话类型

        mIdVerifier.setParameter(SpeechConstant.MFV_SST, "enroll");
// 设置用户id
        mIdVerifier.setParameter(SpeechConstant.AUTH_ID, uuid);
// 设置监听器，开始会话
        mIdVerifier.startWorking(object : IdentityListener{
            override fun onResult(p0: IdentityResult?, p1: Boolean) {
                Log.e("RegisterActivity","${p0?.getResultString()}  ${p1.toString()}")
                addToGroup(uuid)
            }

            override fun onEvent(p0: Int, p1: Int, p2: Int, p3: Bundle?) {
                Log.e("RegisterActivity","event $p0  $p1  $p2 ")
            }

            override fun onError(p0: SpeechError?) {
                Log.e("RegisterActivity","Error " + p0?.printStackTrace())
            }

        });
        // 写入数据，data为图片的二进制数据
        mIdVerifier.writeData("ifr", "", bytes, 0, bytes.size );

        mIdVerifier.stopWrite("ifr");
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

    fun animatedcancleCamera(){

        take.animate().setDuration(300).x(-camera.width.toFloat()).start()

        camera.animate().setDuration(300).x(-camera.width.toFloat()).setListener(object : Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                normal.toggle()
                take.toggle()
            }

        }).start()

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
                take.animate().x(cv_add.right.toFloat()+take.width).alpha(1f).setDuration(800).start()
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
        animateRevealClose()
    }
}
