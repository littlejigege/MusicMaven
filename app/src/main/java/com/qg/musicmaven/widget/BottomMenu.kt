package com.qg.musicmaven.widget

import android.content.Context
import android.support.design.widget.BottomSheetDialog
import android.view.LayoutInflater
import android.view.View
import com.qg.musicmaven.App
import com.qg.musicmaven.R
import kotlinx.android.synthetic.main.dialog_bottom_sheet.view.*

/**
 * Created by jimiji on 2017/12/14.
 */
class BottomMenu(ctx: Context, action: (Int) -> Unit) {
    companion object {
        val PLAY = 0
        val STANDER = 1
        val HQ = 2
    }

    private val dialog = BottomSheetDialog(ctx)

    init {
        val contentView = LayoutInflater.from(ctx).inflate(R.layout.dialog_bottom_sheet, null, false)
        contentView.textViewStander.setOnClickListener {
            dialog.dismiss()
            action(STANDER)
        }
        contentView.textViewPlay.setOnClickListener {
            dialog.dismiss()
            action(PLAY)
        }
        contentView.textViewHQ.setOnClickListener {
            dialog.dismiss()
            action(HQ)
        }
        dialog.setContentView(contentView)
        dialog.show()
    }
}