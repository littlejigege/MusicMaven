package com.qg.musicmaven

import android.os.Bundle
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.iflytek.cloud.*

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.qg.musicmaven", appContext.packageName)
        addGroup("AAAAA")
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
            override fun onEvent(p0: Int, p1: Int, p2: Int, p3: Bundle?) {

            }

            override fun onResult(p0: IdentityResult?, p1: Boolean) {
                System.out.print(p0.toString())
            }


            override fun onError(p0: SpeechError?) {
            }

        });
    }
}
