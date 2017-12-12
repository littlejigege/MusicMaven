package com.qg.musicmaven.base;

import android.support.annotation.NonNull;

/**
 * Created by jimji on 17-11-23.
 */

public abstract class AbsBasePresenter<T extends BaseView> implements BasePresenter<T> {
    protected T view;

    public void dropView() {
        view = null;
    }

    public void takeView(@NonNull T view) {
        this.view = view;
    }
}
