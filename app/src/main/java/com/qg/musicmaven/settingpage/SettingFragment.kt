package com.qg.musicmaven.settingpage

import android.os.Bundle
import android.app.Fragment
import android.preference.PreferenceFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobile.utils.Preference
import com.mobile.utils.inUiThread
import com.mobile.utils.permission.Permission
import com.qg.musicmaven.R
import com.qg.musicmaven.mainpage.TestMainActivity
import com.qg.musicmaven.utils.FilePicker
import kotlinx.android.synthetic.main.frag_settings.view.*
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * Created by jimji on 17-11-23.
 */
class SettingFragment : PreferenceFragment() {
    companion object {
        val TAG = 3
    }

    lateinit var path: android.preference.Preference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TestMainActivity.fragMentShowingTag = TAG
        addPreferencesFromResource(R.xml.pref_settings)
        setupPreference()

    }

    /**
     * 准备好各项设置
     */
    private fun setupPreference() {
        path = findPreference("PATH")
        //同步summary
        path.setOnPreferenceChangeListener { _, newValue ->
            path.summary = newValue as CharSequence?
            true
        }
        //第一次手动同步summary
        path.onPreferenceChangeListener
                .onPreferenceChange(path, activity.defaultSharedPreferences.getString(path.key, ""))
    }

    //private lateinit var rootView: View
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        TestMainActivity.fragMentShowingTag = TAG
//        rootView = inflater.inflate(R.layout.frag_settings, container, false)
//        rootView.setPathButton.onClick {
//            //获取权限之后再打开，保险
//            Permission.STORAGE.doAfterGet(activity) {
//                inUiThread {
//                    FilePicker(activity, fragmentManager,FilePicker.PICK_AUDIO).show()
//                }
//            }
//        }
//        return rootView
//    }
}