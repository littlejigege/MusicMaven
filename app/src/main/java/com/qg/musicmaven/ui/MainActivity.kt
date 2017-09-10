package com.qg.musicmaven.ui

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.qg.musicmaven.App
import com.qg.musicmaven.R
import com.qg.musicmaven.modle.AudioInfo
import com.qg.musicmaven.modle.AudioInfoContainer
import com.qg.musicmaven.modle.FeedBack
import com.qg.musicmaven.netWork.KuGouApi
import com.qg.musicmaven.ui.adapter.AudioAdapter
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import retrofit2.Retrofit
import utils.showToast


class MainActivity : BaseActivity(), MaterialSearchView.OnQueryTextListener {
    val kugouApi by lazy { App.retrofit.create(KuGouApi::class.java) }
    lateinit var adapter: AudioAdapter

    override fun onQueryTextSubmit(query: String): Boolean {
        showToast(query)
        kugouApi.getAudioInfoList(query)
                .subscribeOn(IoScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<FeedBack<AudioInfoContainer>?> {
                    override fun onNext(t: FeedBack<AudioInfoContainer>) {
                        t.data.list.forEach { Log.d(it.songName, it.singerName) }
                        adapter.data.clear()
                        adapter.data.addAll(t.data.list)
                        adapter.notifyDataSetChanged()
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }
                })
        searchView.closeSearch()
        return true

    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //FilePicker(this, fragmentManager).show()
        initView()
    }


    private fun initView() {
        setActionBar(toolbar)
        animateToolbar()
        adapter = AudioAdapter(mutableListOf(), this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        scrim.onClick {
            if (searchView.isSearchOpen){
                searchView.closeSearch()
                scrim.visibility = View.GONE
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        searchView.setMenuItem(menu!!.findItem(R.id.action_search))
        searchView.setOnQueryTextListener(this)
        searchView.setOnSearchViewListener(object: MaterialSearchView.SearchViewListener {
            override fun onSearchViewClosed() {
                scrim.visibility = View.GONE
            }

            override fun onSearchViewShown() {
                scrim.visibility = View.VISIBLE
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        showToast("in")
        when (item.itemId) {
            R.id.action_search -> {

            }
            R.id.action_setting -> {
                startActivity<SettingsActivity>()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun animateToolbar() {
        val t = toolbar.getChildAt(0)
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

    override fun onBackPressed() {
        if (searchView.isSearchOpen) {
            searchView.closeSearch()
            scrim.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }

    fun onSearchBAck(){

    }
}
