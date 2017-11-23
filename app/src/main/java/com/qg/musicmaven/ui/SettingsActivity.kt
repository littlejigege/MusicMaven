package com.qg.musicmaven.ui

import android.graphics.Color

import android.os.Bundle
import android.view.MenuItem
import com.mobile.utils.*
import com.mobile.utils.permission.Permission
import com.qg.musicmaven.App
import com.qg.musicmaven.R
import com.qg.musicmaven.utils.FilePicker
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import kotlinx.android.synthetic.main.activity_setting.*
import org.jetbrains.anko.sdk25.coroutines.onCheckedChange
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.textColor


class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initView()
    }

    private fun initView() {
        if (Preference.get("user", "userId" to -1L) == -1L)
            logoutButton.gone()
        else {
            logoutButton.visiable()
            logoutButton.onClick {
                Preference.save("user") { "userId" - -1L }
                "成功退出登陆".toast()
                logoutButton.gone()
            }
        }
        topBar.setTitle("设置").textColor = Color.WHITE
        topBar.addLeftBackImageButton().onClick { finish() }
        initSettingList()
    }

    private fun initSettingList() {
        val nightMode = settingList.createItemView("开启夜间模式")
        nightMode.setDetailText("已关闭")
        nightMode.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_SWITCH
        nightMode.switch.onCheckedChange { _, isChecked ->
            if (isChecked) {
                nightMode.setDetailText("已开启")
            } else {
                nightMode.setDetailText("已关闭")
            }
        }
        val saveMode = settingList.createItemView("开启省流量模式")
        saveMode.setDetailText("已关闭")
        saveMode.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_SWITCH
        saveMode.switch.onCheckedChange { _, isChecked ->
            if (isChecked) {
                saveMode.setDetailText("已开启")
            } else {
                saveMode.setDetailText("已关闭")
            }
        }
        val downloadPath = settingList.createItemView("设置储存位置")
        downloadPath.orientation = QMUICommonListItemView.VERTICAL
        downloadPath.setDetailText(App.DOWNLOAD_PATH)
        downloadPath.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON
        QMUIGroupListView.newSection(this).setTitle("通用设置")
                .addItemView(nightMode, {})
                .addItemView(saveMode, {})
                .addTo(settingList)
        QMUIGroupListView.newSection(this).setTitle("下载相关")
                .addItemView(downloadPath, {

                    Permission.STORAGE.doAfterGet(this) {
                        inUiThread {
                            FilePicker(this, fragmentManager)
                                    .onFinish { downloadPath.setDetailText(App.DOWNLOAD_PATH) }
                                    .show()
                        }
                    }

                })
                .addTo(settingList)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
