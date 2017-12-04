package com.qg.musicmaven;

/**
 * Created by jimji on 17-11-23.
 */

public abstract class BasePresenter<T> {
    protected T view;

    public void dropView() {
        view = null;
    }

    public void takeView(T view) {
        this.view = view;
    }
}
