package com.qg.musicmaven.ui.searchpage

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.qg.musicmaven.R
import com.qg.musicmaven.base.BaseActivity
import kotlinx.android.synthetic.main.activity_search.*
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.find


class SearchActivity : AppCompatActivity() {
    private lateinit var searhView: SearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbarActivitySearch)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        //拿到searchView
        searhView = MenuItemCompat.getActionView(menu.findItem(R.id.menu_search_search)) as SearchView
        //让他出来
        searhView.onActionViewExpanded()
        //添加提交按钮
        searhView.isSubmitButtonEnabled = true
        //修改背景颜色
        searhView.find<SearchView.SearchAutoComplete>(R.id.search_src_text).background = getDrawable(R.drawable.round_rect)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }
}
