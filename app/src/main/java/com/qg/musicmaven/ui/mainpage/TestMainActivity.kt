package com.qg.musicmaven.ui.mainpage


import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.app.Fragment
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.mobile.utils.isConnected
import com.qg.musicmaven.MyMusicService
import com.qg.musicmaven.R
import com.qg.musicmaven.base.BaseActivity
import com.qg.musicmaven.cloudpage.CloudFragment
import com.qg.musicmaven.download.ApkDownloadUtil
import com.qg.musicmaven.dreampage.DreamFragment

import com.qg.musicmaven.mainpage.MainPagePresenter
import com.qg.musicmaven.modle.bean.UpdateInfo

import com.qg.musicmaven.settingpage.SettingFragment
import com.qg.musicmaven.ui.searchpage.SearchActivity
import com.qg.musicmaven.widget.UploadDialog
import kotlinx.android.synthetic.main.activity_test_main.*
import me.drakeet.materialdialog.MaterialDialog
import org.jetbrains.anko.downloadManager
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startService


class TestMainActivity : BaseActivity(), MainPageContract.View {


    //默认为酷狗碎片
    companion object {
        var fragMentShowingTag = CloudFragment.TAG
    }

    private val presenter by lazy { MainPagePresenter() }
    //四个碎片
    private val cloudFragment by lazy { CloudFragment() }
    private val dreamFragment by lazy { DreamFragment() }
    private val settingFragment by lazy { SettingFragment() }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_cloud -> {
                presenter.onCloudClick()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dream -> {
                presenter.onDreamClick()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                presenter.onSettingClick()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun animateToolbar() {
        val t = toolbarMain.getChildAt(0)
        if (t != null && t is TextView) {
            val title = t
            title.alpha = 0f
            title.scaleX = 0.8f
            title.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .setStartDelay(300)
                    .setDuration(900).interpolator = AccelerateDecelerateInterpolator()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_main)
        presenter.takeView(this)
        setActionBar(toolbarMain)
        navigationBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //toolbar标题做动画
        animateToolbar()
        //加载第一个显示的碎片
        presenter.onCloudClick()
        //请求公告
        presenter.requestNotice()
        //检查更新
        presenter.checkForUpdate()
        startService(Intent(this, MyMusicService::class.java))
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.test_main, menu)
        //searchViewMain.setMenuItem(menu!!.findItem(R.id.test_main_search))
        //searchViewMain.setOnQueryTextListener(this)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.test_main_search -> {
                startActivity<SearchActivity>()
                true
            }
            R.id.test_main_upload -> {
                UploadDialog().show(fragmentManager, "")
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }


    private fun replaceFragment(fragment: Fragment) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(fragmentContainer.id, fragment)
        transaction.commit()
    }

    override fun onDestroy() {
        presenter.dropView()
        super.onDestroy()
    }


    override fun openCloudPage() {
        replaceFragment(cloudFragment)
    }

    override fun openDreamPage() {
        replaceFragment(dreamFragment)
    }

    override fun openSettingPage() {
        replaceFragment(settingFragment)
    }

    override fun onNoticeGet(notice: String?) {
        runTextView.text = notice
    }

    override fun onUpdateGet(info: UpdateInfo?) {
        info?.let {
            val dialog = MaterialDialog(this)
            dialog.setTitle("版本更新")
                    .setMessage(it.statement)
                    .setPositiveButton("下载安装", {
                        dialog.dismiss()
                        //下载
                        ApkDownloadUtil(downloadManager).downloadApk(info.versionUrl)
                    })
                    .setNegativeButton("不了", {
                        dialog.dismiss()
                    })
                    .show()
        }
    }
}
