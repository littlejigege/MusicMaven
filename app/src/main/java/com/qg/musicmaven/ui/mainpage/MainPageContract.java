package com.qg.musicmaven.ui.mainpage;

import com.qg.musicmaven.base.BaseView;

/**
 * Created by jimji on 17-11-23.
 */

public interface MainPageContract {
    interface View extends BaseView {
        void openKugouPage();

        void openCloudPage();

        void openDreamPage();

        void openSettingPage();
    }


}
