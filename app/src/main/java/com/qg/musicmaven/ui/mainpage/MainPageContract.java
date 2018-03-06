package com.qg.musicmaven.ui.mainpage;

import com.qg.musicmaven.base.BaseView;
import com.qg.musicmaven.modle.bean.UpdateInfo;

/**
 * Created by jimji on 17-11-23.
 */

public interface MainPageContract {
    interface View extends BaseView {

        void openCloudPage();

        void openDreamPage();

        void openSettingPage();

        void onNoticeGet(String notice);

        void onUpdateGet(UpdateInfo info);
    }


}
