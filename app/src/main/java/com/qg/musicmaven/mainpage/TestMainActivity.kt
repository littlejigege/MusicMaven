package com.qg.musicmaven.mainpage

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.qg.musicmaven.R
import com.qg.musicmaven.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_test_main.*

class TestMainActivity : BaseActivity() {
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_kugou -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_cloud -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dream -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_main)
        setActionBar(toolbarMain)
        navigationBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.test_main,menu)
        searchViewMain.setMenuItem(menu!!.findItem(R.id.test_main_search))
        //searchViewMain.setOnQueryTextListener(this)
        return true
    }
}
