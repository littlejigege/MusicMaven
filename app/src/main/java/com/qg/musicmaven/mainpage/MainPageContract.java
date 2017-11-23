package com.qg.musicmaven.mainpage;

import android.support.v7.widget.SearchView;

import com.qg.musicmaven.BasePresenter;
import com.qg.musicmaven.BaseView;

/**
 * Created by jimji on 17-11-23.
 */

public interface MainPageContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter<View> {

    }
}
