package com.qg.musicmaven.widget

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface

/**
 * Created by jimiji on 2017/12/14.
 */
class UpLoadProgressDialog(ctx: Context) {
    private val dialog = ProgressDialog(ctx)
    var isCanceled: Boolean = false

    init {
        with(dialog) {
            setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            setTitle("愿望实现中...")
            max = 100
            setButton(DialogInterface.BUTTON_NEGATIVE, "取消", DialogInterface.OnClickListener({ dialog, which ->
                isCanceled = true
            }))
        }
    }

    fun show() {
        dialog.show()
    }


    fun updateProgress(process: Int) {
        if (process == 100) {
            dialog.dismiss()
            return
        }
        dialog.progress = process
    }

}