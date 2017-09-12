package com.qg.musicmaven.utils

import android.app.Activity
import android.app.FragmentManager
import com.codekidlabs.storagechooser.Content
import com.codekidlabs.storagechooser.StorageChooser
import com.qg.musicmaven.App

/**
 * Created by 小吉哥哥 on 2017/9/7.
 */
class FilePicker(var act: Activity, var fragM: FragmentManager) {
    private val mBuilder = StorageChooser.Builder()
    lateinit var mChooser: StorageChooser
    private val c = Content()
    private var _onFinish: () -> Unit = {}
    fun onFinish(f: () -> Unit): FilePicker {
        _onFinish = f
        return this
    }

    init {
        setup()
        build(c)
        setAction()
    }

    private fun setup() {
        mBuilder.withMemoryBar(true)
                .allowAddFolder(true)
                .allowCustomPath(true)
                .setType(StorageChooser.DIRECTORY_CHOOSER)
                .skipOverview(true)

        c.createLabel = "创建"
        c.cancelLabel = "取消"
        c.selectLabel = "确定"
        c.textfieldHintText = "输入文件夹名称"
        c.textfieldErrorText = "输入为空"
    }

    private fun build(c: Content) {
        mChooser = mBuilder.withActivity(act)
                .withFragmentManager(fragM)
                .withContent(c)
                .build()
    }

    private fun setAction() = mChooser.setOnSelectListener {
        App.DOWNLOAD_PATH = it
        _onFinish()
    }

    fun show() = mChooser.show()

}