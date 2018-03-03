package com.qg.musicmaven.rlogin

import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.iflytek.cloud.*
import com.qg.musicmaven.App
import com.qg.musicmaven.base.AbsBasePresenter
import com.qg.musicmaven.modle.bean.RegisterBody
import com.qg.musicmaven.modle.bean.VerifyResult
import com.qg.musicmaven.utils.Fetcher
import java.util.*

/**
 * Created by steve on 17-12-12.
 */
class RegPresenter : AbsBasePresenter<RegContract.View>(), RegContract.Presenter {

    val fetcher = Fetcher()
    private val GROUP_ID = "3622740854"
    override fun getCode(email: String) {
        fetcher.fetchIO(App.serverApi.getCode(email))
    }

    override fun normalregister(method: String, email: String, password: String, count: String, faceId: String) {
        fetcher.fetchIO(App.serverApi.register(RegisterBody(method,
                RegisterBody.Data("mavenUser${System.currentTimeMillis()}", email, password, count, faceId))),
                onNext = { feedBack ->

                    when (feedBack.status) {
                        1 -> view.registerSuccess()
                        400 -> view.alreadyRegister(faceId)
                        50 -> view.onError(RuntimeException("参数错误"))
                        else -> view.onError(RuntimeException("建成奇奇怪怪的异常" + feedBack.toString()))

                    }

                }, onError = { e -> view.onError(e) })

    }

    override fun register(bytes: ByteArray) {

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
                    if (result.ifv_result.candidates[0].score > 90) {
                        Log.e("RegPresenter normal", result.toString())
                        normalregister("1", "", "", "", faceId = result.ifv_result.candidates[0].uuid)
                    } else {
                        reg(bytes)
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

    private fun reg(bytes: ByteArray) {

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
                if (p1) {
                    addToGroup(uuid)
                }
            }

            override fun onEvent(p0: Int, p1: Int, p2: Int, p3: Bundle?) {
            }

            override fun onError(p0: SpeechError?) {
                view.onError(p0!!)
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
        val params = "scope=person,group_id=" + GROUP_ID + ",auth_id=" + uuid;
        val cmd = "add";


        // cmd 为 操作，模型管理包括 query, delete, download，组管理包括 add，query，delete
        mIdVerifier.execute("ipt", cmd, params, object : IdentityListener {
            override fun onResult(p0: IdentityResult?, success: Boolean) {
                Log.e("RegPresenteraddtogroup", p0?.resultString)
                if (!success) {
                    view.onError(RuntimeException("加入组失败"))
                } else {
                    //服务器注册成功
                    normalregister("1", faceId = uuid)
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
            }

            override fun onError(p0: SpeechError?) {
            }

        });
    }
}