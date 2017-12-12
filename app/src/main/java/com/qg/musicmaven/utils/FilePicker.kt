package com.qg.musicmaven.utils

import android.app.Activity
import android.app.FragmentManager
import com.codekidlabs.storagechooser.Content
import com.codekidlabs.storagechooser.StorageChooser
import com.qg.musicmaven.App

/**
 * Created by 小吉哥哥 on 2017/9/7.
 */
class FilePicker(var act: Activity, var fragM: FragmentManager, val type: Int) {
    companion object {
        val PICK_PAHT = 0
        val PICK_AUDIO = 1
    }

    private val mBuilder = StorageChooser.Builder()
    lateinit var mChooser: StorageChooser
    private val c = Content()
    private var _onFinish: (MutableList<String>) -> Unit = {}
    fun onFinish(f: (MutableList<String>) -> Unit): FilePicker {
        _onFinish = f
        return this
    }

    init {
        setup()
        build(c)
        setAction()
    }

    private fun setup() {
        if (type == PICK_PAHT) {
            mBuilder.withMemoryBar(true)
                    .allowAddFolder(true)
                    .allowCustomPath(true)
                    .setType(StorageChooser.DIRECTORY_CHOOSER)
                    .skipOverview(true)
            //.filter(StorageChooser.FileType.AUDIO)

            c.createLabel = "创建"
            c.cancelLabel = "取消"
            c.selectLabel = "确定"
            c.textfieldHintText = "输入文件夹名称"
            c.textfieldErrorText = "输入为空"
        } else {
            mBuilder.withMemoryBar(true)
                    .allowAddFolder(false)
                    .allowCustomPath(true)
                    .setType(StorageChooser.FILE_PICKER)
                    .skipOverview(true)
            c.cancelLabel = "取消"
            c.selectLabel = "确定"
        }

    }

    private fun build(c: Content) {
        mChooser = mBuilder.withActivity(act)
                .withFragmentManager(fragM)
                .withContent(c)
                .build()
    }

    private fun setAction() {
        mChooser.setOnSelectListener {
            if (type == PICK_PAHT) {
                _onFinish(mutableListOf(it))
            } else {
                _onFinish(mutableListOf(it))
            }

        }
        mChooser.setOnMultipleSelectListener {
            _onFinish(it.toMutableList())
        }

    }

    fun show() = mChooser.show()

}