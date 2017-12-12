package com.qg.musicmaven.rlogin

import com.qg.musicmaven.base.AbsBasePresenter
import com.qg.musicmaven.base.BasePresenter
import com.qg.musicmaven.base.BaseView

/**
 * Created by steve on 17-12-12.
 */
interface RLContract {

    interface View : BaseView {

    }

    interface Presenter : BasePresenter<View>{

    }


}