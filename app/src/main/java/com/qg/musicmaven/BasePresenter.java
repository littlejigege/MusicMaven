package com.qg.musicmaven;

/**
 * Created by jimji on 17-11-23.
 */

public interface BasePresenter<T> {
    void dropView();
    void takeView(T view);
}
