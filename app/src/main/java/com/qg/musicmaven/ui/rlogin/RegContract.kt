package com.qg.musicmaven.rlogin

import com.qg.musicmaven.base.AbsBasePresenter
import com.qg.musicmaven.base.BasePresenter
import com.qg.musicmaven.base.BaseView
import com.qg.musicmaven.modle.bean.VerifyResult

/**
 * Created by steve on 17-12-12.
 */
interface RegContract {

    interface View : BaseView {
        fun onError(e : Throwable)
        fun alreadyRegister(uuid:String)
        fun registerSuccess()
    }

    interface Presenter : BasePresenter<View>{
        fun register(bytes: ByteArray)
        fun normalregister(method : String,  email:String="",password:String="" ,count : String="",faceId:String="")
        fun getCode(email: String)
    }


}