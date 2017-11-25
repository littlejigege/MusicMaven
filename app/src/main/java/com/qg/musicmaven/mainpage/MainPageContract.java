package com.qg.musicmaven.mainpage;

import android.support.v7.widget.SearchView;

import com.qg.musicmaven.BasePresenter;
import com.qg.musicmaven.BaseView;

/**
 * Created by jimji on 17-11-23.
 */

public interface MainPageContract {
    interface View extends BaseView<Presenter> {
        void openKugouPage();

        void openCloudPage();

        void openDreamPage();

        void openSettingPage();
    }

    interface Presenter extends BasePresenter<View> {
        void onKugouClick();

        void onCloudClick();

        void onDreamClick();

        void onSettingClick();
    }
}
