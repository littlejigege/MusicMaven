package com.qg.musicmaven.dreampage

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobile.utils.showToast
import com.qg.musicmaven.R
import com.qg.musicmaven.ui.dreampage.EditWishDialog
import com.qg.musicmaven.ui.dreampage.WishActivity
import com.qg.musicmaven.ui.mainpage.TestMainActivity
import com.qg.musicmaven.widget.BottomMenu
import kotlinx.android.synthetic.main.frag_dream.view.*
import org.jetbrains.anko.startActivity

/**
 * Created by jimji on 17-11-23.
 */
class DreamFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View) {
        when (v.id) {
            R.id.cardViewWish -> {
                EditWishDialog(activity) {
                    //TODO 上传愿望
                    println(it)
                }
            }
            else -> {
                startActivity<WishActivity>()
            }
        }
    }

    companion object {
        val TAG = 2
    }

    private lateinit var rootView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        TestMainActivity.fragMentShowingTag = TAG
        rootView = inflater.inflate(R.layout.frag_dream, container, false)
        rootView.cardViewWish.setOnClickListener(this)
        rootView.cardVIewAchieve.setOnClickListener(this)
        return rootView
    }
}