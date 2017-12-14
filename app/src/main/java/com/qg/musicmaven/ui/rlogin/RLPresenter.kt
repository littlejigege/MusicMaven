package com.qg.musicmaven.rlogin

import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.iflytek.cloud.*
import com.iflytek.speech.VerifierResult
import com.qg.musicmaven.App
import com.qg.musicmaven.base.AbsBasePresenter
import com.qg.musicmaven.modle.bean.VerifyResult
import java.util.*

/**
 * Created by steve on 17-12-12.
 */
class RLPresenter : AbsBasePresenter<RLContract.View>(), RLContract.Presenter {

    private val GROUP_ID = "3622740854"

    companion object {
        //注册前想看看有没有重复的,防止重复注册
        val TYPE_PREREGISTER = 1;

        //真正的注册
        val TYPE_LOGIN = 2;
    }

    override fun verify(bytes: ByteArray, type: Int) {

        // 指定组id，最相似结果数
        val params = "group_id=" + GROUP_ID

        val mIdVerifier = IdentityVerifier.createVerifier(App.instance) {}
        // 设置业务场景
        mIdVerifier.setParameter(SpeechConstant.MFV_SCENES, "ifr");

        // 设置业务类型：鉴别（identify）
        mIdVerifier.setParameter(SpeechConstant.MFV_SST, "identify");

        // 设置监听器，开始会话
        mIdVerifier.startWorking(object : IdentityListener {
            override fun onResult(p0: IdentityResult?, success: Boolean) {

                if (success) {

                    val result = Gson().fromJson(p0?.getResultString(), VerifyResult::class.java)
                    when (type) {
                        TYPE_PREREGISTER ->
                            if (result.ifv_result.candidates[0].score > 90) {
                                view.alreadyRegister()
                            }

                        TYPE_LOGIN ->
                            if (result.ifv_result.candidates[0].score > 90) {
                                view.loginSuccess(result.ifv_result.candidates[0])
                            } else {
                                view.loginFailed()
                            }
                    }
                } else {
                    view.onError(RuntimeException("未知错误"))
                }
            }

            override fun onEvent(p0: Int, p1: Int, p2: Int, p3: Bundle?) {}

            override fun onError(p0: SpeechError) {
                view.onError(p0)
            }

        });


        // 写入数据
        mIdVerifier.writeData("ifr", params, bytes, 0, bytes.size);

        mIdVerifier.stopWrite("ifr");
    }

    override fun register(bytes: ByteArray) {

        val uuid = UUID.randomUUID().toString().replace("-", "")
        Log.e("UUID", uuid)
        val mIdVerifier = IdentityVerifier.createVerifier(App.instance) {}
        // 设置会话场景
        mIdVerifier.setParameter(SpeechConstant.MFV_SCENES, "ifr");
        // 设置会话类型

        mIdVerifier.setParameter(SpeechConstant.MFV_SST, "enroll");
        // 设置用户id
        mIdVerifier.setParameter(SpeechConstant.AUTH_ID, uuid);
        // 设置监听器，开始会话
        mIdVerifier.startWorking(object : IdentityListener {
            override fun onResult(p0: IdentityResult?, p1: Boolean) {
                Log.e("RegisterActivity", "${p0?.getResultString()}  ${p1.toString()}")
                addToGroup(uuid)
            }

            override fun onEvent(p0: Int, p1: Int, p2: Int, p3: Bundle?) {
                Log.e("RegisterActivity", "event $p0  $p1  $p2 ")
            }

            override fun onError(p0: SpeechError?) {
                Log.e("RegisterActivity", "Error " + p0?.printStackTrace())
            }

        });
        // 写入数据，data为图片的二进制数据
        mIdVerifier.writeData("ifr", "", bytes, 0, bytes.size);
        mIdVerifier.stopWrite("ifr");
    }

    private fun addToGroup(uuid: String) {
        val mIdVerifier = IdentityVerifier.createVerifier(App.instance) {}
        mIdVerifier.setParameter(SpeechConstant.MFV_SCENES, "ipt");
        // 设置用户id
        mIdVerifier.setParameter(SpeechConstant.AUTH_ID, uuid);
        // params 根据不同的操作而不同

        //创建组
        val params = "scope=person,group_id=" + "3622740854" + ",auth_id=" + uuid;
        val cmd = "add";


        // cmd 为 操作，模型管理包括 query, delete, download，组管理包括 add，query，delete
        mIdVerifier.execute("ipt", cmd, params, object : IdentityListener {
            override fun onResult(p0: IdentityResult?, success: Boolean) {
                if (success) {
                    view.onError(RuntimeException("未知错误"))
                }
            }

            override fun onEvent(p0: Int, p1: Int, p2: Int, p3: Bundle?) {}

            override fun onError(p0: SpeechError) {
                view.onError(p0)
            }

        });
    }

    private fun addGroup(groupName: String) {
        val mIdVerifier = IdentityVerifier.createVerifier(App.instance) {}

        mIdVerifier.setParameter(SpeechConstant.MFV_SCENES, "ipt");

        // 设置用户id
        mIdVerifier.setParameter(SpeechConstant.AUTH_ID, "system_user008");

        // params 根据不同的操作而不同

        //创建组
        val params = "scope=group,group_name=" + groupName;
        val cmd = "add";


        // cmd 为 操作，模型管理包括 query, delete, download，组管理包括 add，query，delete
        mIdVerifier.execute("ipt", cmd, params, object : IdentityListener {
            override fun onResult(p0: IdentityResult?, p1: Boolean) {
                Log.e("RegisterActivity", "${p0?.getResultString()}  ${p1.toString()} 成功查ungjianzuzu组 $groupName")
            }

            override fun onEvent(p0: Int, p1: Int, p2: Int, p3: Bundle?) {
                Log.e("RegisterActivity", "event $p0  $p1  $p2 ")
            }

            override fun onError(p0: SpeechError?) {
                Log.e("RegisterActivity", "Error  " + p0?.printStackTrace())
            }

        });
    }
}