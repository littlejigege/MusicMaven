package com.qg.musicmaven.cloudpage;

import com.qg.musicmaven.BasePresenter;
import com.qg.musicmaven.BaseView;

/**
 * Created by jimji on 17-11-23.
 */

public interface CloudContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter<View> {

    }
}
