package com.qg.musicmaven.settingpage

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobile.utils.inUiThread
import com.mobile.utils.permission.Permission
import com.qg.musicmaven.R
import com.qg.musicmaven.mainpage.TestMainActivity
import com.qg.musicmaven.utils.FilePicker
import kotlinx.android.synthetic.main.frag_settings.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * Created by jimji on 17-11-23.
 */
class SettingFragment : Fragment() {
    companion object {
        val TAG = 3
    }

    private lateinit var rootView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        TestMainActivity.fragMentShowingTag = TAG
        rootView = inflater.inflate(R.layout.frag_settings, container, false)
        rootView.setPathButton.onClick {
            //获取权限之后再打开，保险
            Permission.STORAGE.doAfterGet(activity) {
                inUiThread {
                    FilePicker(activity, fragmentManager).show()
                }
            }
        }
        return rootView
    }
}