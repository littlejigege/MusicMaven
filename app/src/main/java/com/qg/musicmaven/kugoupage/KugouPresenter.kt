package com.qg.musicmaven.kugoupage

import com.qg.musicmaven.BasePresenter
import com.qg.musicmaven.modle.SearchAcitonCreator
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by jimiji on 2017/12/3.
 */
class KugouPresenter : BasePresenter<IView>() {
    private val pageNext = AtomicInteger(2)
    var keyWord = ""
    /**
     * 加载更多
     */
    fun loadMore() {
        SearchAcitonCreator.searchFromKugou(keyWord, pageNext.getAndIncrement()) {
            println(it)
            if (it.isEmpty()) {
                view.onNoMore()
            } else {
                view.onLoadMore(it)
            }

        }
    }

}