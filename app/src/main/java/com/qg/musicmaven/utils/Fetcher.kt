package com.qg.musicmaven.utils

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.Android

/**
 * Created by steve on 17-11-25.
 */


class Fetcher constructor() {

    enum class Type {
        NEW, IO, MAIN
    }

    private val dispoable = CompositeDisposable()

    fun <T> fetchNew(source: Observable<T>,
                     onNext: (T) -> Unit = {},
                     onError: (Throwable) -> Unit = {},
                     onComplete: () -> Unit = {},
                     onSubscribe: (Disposable) -> Unit = {}) {

        dispoable.add(source.fetchNewThread(onNext, onError, onComplete, onSubscribe))
    }

    fun <T> fetchIO(source: Observable<T>,
                    onNext: (T) -> Unit = {},
                    onError: (Throwable) -> Unit = { it.printStackTrace() },
                    onComplete: () -> Unit = {},
                    onSubscribe: (Disposable) -> Unit = {}) {

        source.fetchIO(onNext, onError, onComplete, onSubscribe)
    }

    fun <T> fetchMain(source: Observable<T>,
                      onNext: (T) -> Unit = {},
                      onError: (Throwable) -> Unit = {},
                      onComplete: () -> Unit = {},
                      onSubscribe: (Disposable) -> Unit = {}) {

        source.fetchMainthread(onNext, onError, onComplete, onSubscribe)
    }

    fun <T> fetchComputation(source: Observable<T>,
                             onNext: (T) -> Unit = {},
                             onError: (Throwable) -> Unit = {},
                             onComplete: () -> Unit = {},
                             onSubscribe: (Disposable) -> Unit = {}) {

        source.fetchComputation(onNext, onError, onComplete, onSubscribe)
    }


    fun <T> Observable<T>.fetchIO(onNext: (T) -> Unit = {},
                                  onError: (Throwable) -> Unit = {},
                                  onComplete: () -> Unit = {},
                                  onSubscribe: (Disposable) -> Unit = {}): Disposable =
            subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribe(onNext, onError, onComplete, onSubscribe)

    fun <T> Observable<T>.fetchNewThread(onNext: (T) -> Unit = {},
                                         onError: (Throwable) -> Unit = {},
                                         onComplete: () -> Unit = {}, onSubscribe:
                                         (Disposable) -> Unit = {}): Disposable? =
            subscribeOn(Schedulers.newThread()).
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribe(onNext, onError, onComplete, onSubscribe)


    fun <T> Observable<T>.fetchMainthread(onNext: (T) -> Unit = {},
                                          onError: (Throwable) -> Unit = {},
                                          onComplete: () -> Unit = {},
                                          onSubscribe: (Disposable) -> Unit = {}): Disposable? =
            subscribeOn(AndroidSchedulers.mainThread()).
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribe(onNext, onError, onComplete, onSubscribe)

    fun <T> Observable<T>.fetchComputation(onNext: (T) -> Unit = {},
                                           onError: (Throwable) -> Unit = {},
                                           onComplete: () -> Unit = {},
                                           onSubscribe: (Disposable) -> Unit = {}): Disposable? =
            subscribeOn(Schedulers.computation()).
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribe(onNext, onError, onComplete, onSubscribe)
}

