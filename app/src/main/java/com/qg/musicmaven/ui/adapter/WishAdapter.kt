package com.qg.musicmaven.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qg.musicmaven.R
import com.qg.musicmaven.modle.bean.Wish
import kotlinx.android.synthetic.main.item_wish.view.*

/**
 * Created by jimiji on 2017/12/14.
 */
class WishAdapter(val data: MutableList<Wish>, val ctx: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var _onItemClick: (Wish, View) -> Unit = { _, _ ->  }
    fun onItemClick(action: (Wish, View) -> Unit) {
        _onItemClick = action
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(data[position]) {
            holder.itemView.textViewAudioName.text = "歌手：$songName"
            holder.itemView.textViewSingerNameOfWish.text = "歌名：$singerName"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(ctx).inflate(R.layout.item_wish, parent, false)
        val holder = ViewHolder(itemView)
        itemView.setOnClickListener {
            _onItemClick(data[holder.adapterPosition], itemView)
        }
        return holder
    }

    override fun getItemCount() = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}