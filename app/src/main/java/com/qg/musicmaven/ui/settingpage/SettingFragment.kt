package com.qg.musicmaven.settingpage

import android.os.Bundle
import android.preference.PreferenceFragment
import com.qg.musicmaven.App
import com.qg.musicmaven.R
import com.qg.musicmaven.ui.mainpage.TestMainActivity
import org.jetbrains.anko.defaultSharedPreferences

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
        findPreference("USER_NAME").summary = App.instance.getUser()?.customerName
        findPreference("USER_EMAIL").summary = App.instance.getUser()?.userEmail
    }


}