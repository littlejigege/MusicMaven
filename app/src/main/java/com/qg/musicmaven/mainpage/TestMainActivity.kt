package com.qg.musicmaven.mainpage


import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.app.Fragment
import android.view.Menu
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.qg.musicmaven.R
import com.qg.musicmaven.base.BaseActivity
import com.qg.musicmaven.cloudpage.CloudFragment
import com.qg.musicmaven.dreampage.DreamFragment
import com.qg.musicmaven.kugoupage.KugouFragment
import com.qg.musicmaven.modle.SearchAcitonCreator
import com.qg.musicmaven.settingpage.SettingFragment
import kotlinx.android.synthetic.main.activity_test_main.*


class TestMainActivity : BaseActivity(), MainPageContract.View {

    //默认为酷狗碎片
    companion object {
        var fragMentShowingTag = KugouFragment.TAG
    }

    private val presenter by lazy { MainPagePresenter() }
    //四个碎片
    private val kugouFragment by lazy { KugouFragment() }
    private val cloudFragment by lazy { CloudFragment() }
    private val dreamFragment by lazy { DreamFragment() }
    private val settingFragment by lazy { SettingFragment() }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_kugou -> {
                presenter.onKugouClick()
                return@OnNavigationItemSelectedListener true
            }
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
        presenter.onKugouClick()
        initSearchView()

    }

    /**
     * 初始化搜索框
     */
    private fun initSearchView() {
        searchViewMain.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (fragMentShowingTag == KugouFragment.TAG) {
                    searchViewMain.closeSearch()
                    kugouFragment.showSearching()
                    SearchAcitonCreator.searchFromKugou(query, 1) {
                        kugouFragment.setAudioList(query, it)
                    }
                }

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //当在酷狗中搜索时才需要建议
                if (fragMentShowingTag == KugouFragment.TAG) {
                    //如果关键字不为空就获取建议
                    if (newText.isNotEmpty()) {
                        //先取消再搜索，保证不会错位
                        SearchAcitonCreator.cancelSuggestion()
                        SearchAcitonCreator.getSuggestionFromKugou(newText) {
                            searchViewMain.setSuggestions(it.toTypedArray())
                            //不想保留建议所以没死都重新设置监听器
                            searchViewMain.setOnItemClickListener { _, _, position, _ ->
                                searchViewMain.setQuery(it[position], true)
                                searchViewMain.closeSearch()
                            }
                        }
                    }
                }

                return true
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.test_main, menu)
        searchViewMain.setMenuItem(menu!!.findItem(R.id.test_main_search))
        //searchViewMain.setOnQueryTextListener(this)
        return true
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

    override fun openKugouPage() {
        replaceFragment(kugouFragment)
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
}
