package com.qg.musicmaven.ui.dreampage

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.mobile.utils.onTextChange
import com.mobile.utils.showToast
import com.qg.musicmaven.R
import com.qg.musicmaven.modle.bean.Wish
import kotlinx.android.synthetic.main.dialog_edit_wish.view.*

/**
 * Created by jimiji on 2017/12/13.
 */
class EditWishDialog(ctx: Context, val action: (Wish) -> Unit) : View.OnClickListener {
    override fun onClick(v: View?) {
        if (wish.albumName.isNotEmpty() && wish.singerName.isNotEmpty() && wish.songName.isNotEmpty()) {
            dialog.dismiss()
            action(wish)
        } else {
            showToast("请将信息填写完整")
        }
    }


    private val dialogBuilder = AlertDialog.Builder(ctx)
    private lateinit var dialog: AlertDialog
    var wish = Wish(0)
    private lateinit var contentView: View
    private fun setup() {
        contentView = LayoutInflater.from(dialogBuilder.context).inflate(R.layout.dialog_edit_wish, null, false)
        //监听三个输入框变化
        contentView.inputLayoutSongName.editText?.onTextChange { wish.songName = it }
        contentView.inputLayoutSingerName.editText?.onTextChange { wish.singerName = it }
        contentView.inputLayoutAlbumName.editText?.onTextChange { wish.albumName = it }
        dialogBuilder.setTitle("填写你的愿望")
                .setView(contentView)
                .setPositiveButton("确定", null)
        dialog = dialogBuilder.create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this)
        }
    }

    init {
        setup()
        dialog.show()
    }
}