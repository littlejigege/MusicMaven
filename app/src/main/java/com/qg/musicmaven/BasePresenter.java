package com.qg.musicmaven;

/**
 * Created by jimji on 17-11-23.
 */

public interface BasePresenter<T> {
    void dropView(T view);
    void takeView(T view);
}
