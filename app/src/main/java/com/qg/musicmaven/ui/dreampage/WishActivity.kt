package com.qg.musicmaven.ui.dreampage

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.view.MenuItem
import android.view.View
import com.mobile.utils.inUiThread
import com.mobile.utils.permission.Permission
import com.mobile.utils.permission.PermissionCompatActivity
import com.mobile.utils.showToast
import com.qg.musicmaven.R
import com.qg.musicmaven.modle.bean.Wish
import com.qg.musicmaven.ui.adapter.WishAdapter
import com.qg.musicmaven.utils.FilePicker
import kotlinx.android.synthetic.main.activity_wish.*

class WishActivity : PermissionCompatActivity(), DreamContract.View {


    override fun loadMoreDone(list: MutableList<Wish>) {
        reFreshLayoutWish.finishLoadmore(1)
        adapter.data.addAll(list)
        adapter.notifyDataSetChanged()
    }

    override fun reFreshDone(list: MutableList<Wish>) {
        reFreshLayoutWish.finishRefresh(1)
        adapter = WishAdapter(list, this)
        adapter.onItemClick { wish, view ->
            showPopupMenu(wish, view)
        }
        recyclerViewWish.adapter = adapter
        recyclerViewWish.layoutManager = LinearLayoutManager(this)
    }

    private lateinit var adapter: WishAdapter
    private val presenter = DreamPresenter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wish)
        presenter.takeView(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        reFreshLayoutWish.setOnRefreshListener { presenter.reFresh() }
        reFreshLayoutWish.setOnLoadmoreListener { presenter.loadMore() }
        presenter.reFresh()

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

    private fun showPopupMenu(wish: Wish, view: View) {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.menu_popup)
        popup.setOnMenuItemClickListener { _ ->
            Permission.STORAGE.doAfterGet(this) {
                inUiThread {
                    val picker = FilePicker(this, fragmentManager, FilePicker.PICK_AUDIO)
                    picker.onFinish {
                        presenter.uploadSong(it[0], wish)
                    }
                    picker.show()
                }
            }
            true
        }
        popup.show()
    }
}
