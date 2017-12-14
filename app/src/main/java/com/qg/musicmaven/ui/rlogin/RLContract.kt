package com.qg.musicmaven.rlogin

import com.qg.musicmaven.base.AbsBasePresenter
import com.qg.musicmaven.base.BasePresenter
import com.qg.musicmaven.base.BaseView
import com.qg.musicmaven.modle.bean.VerifyResult

/**
 * Created by steve on 17-12-12.
 */
interface RLContract {

    interface View : BaseView {
        fun onError(e : Throwable);
        fun alreadyRegister()
        fun registerSuccess()
        fun loginSuccess(user:VerifyResult.Candidate)
        fun loginFailed()
    }

    interface Presenter : BasePresenter<View>{
        fun register(bytes: ByteArray)
        fun verify(bytes: ByteArray,type : Int)
    }


}