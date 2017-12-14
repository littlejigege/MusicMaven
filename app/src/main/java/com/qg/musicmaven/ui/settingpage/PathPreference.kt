package com.qg.musicmaven.settingpage

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.preference.Preference
import android.support.v4.app.Fragment
import android.util.AttributeSet
import com.mobile.utils.inUiThread
import com.mobile.utils.permission.Permission
import com.mobile.utils.showToast
import com.qg.musicmaven.modle.QiNiu
import com.qg.musicmaven.utils.FilePicker

/**
 * Created by jimiji on 2017/12/4.
 */
class PathPreference : Preference {
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
    }

    constructor(context: Context?) : super(context)

    override fun onSetInitialValue(restorePersistedValue: Boolean, defaultValue: Any?) {
        super.onSetInitialValue(restorePersistedValue, defaultValue)
        if (!restorePersistedValue) {
            persistString(defaultValue as String)
        }
    }

    override fun onGetDefaultValue(a: TypedArray, index: Int): Any {
        return a.getString(index)
    }

    override fun onClick() {
        Permission.STORAGE.doAfterGet(context as Activity) {
            inUiThread {

                val picker = FilePicker(context as Activity, (context as Activity).fragmentManager, FilePicker.PICK_PAHT)
                picker.onFinish {
                    persistString(it[0])
                    callChangeListener(it[0])
                }
                picker.show()
            }
        }

        super.onClick()
    }
}