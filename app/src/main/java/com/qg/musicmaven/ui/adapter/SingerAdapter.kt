package com.qg.musicmaven.ui.adapter

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.qg.musicmaven.App
import com.qg.musicmaven.R
import com.qg.musicmaven.modle.bean.Singer
import com.qg.musicmaven.ui.cloudpage.singerpage.SingerActivity
import kotlinx.android.synthetic.main.item_singer.view.*

/**
 * Created by jimiji on 2017/12/14.
 */
class SingerAdapter(val data: MutableList<Singer>, val ctx: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(ctx).inflate(R.layout.item_singer, parent, false)
        val holder = ViewHolder(itemView)
        //点击启动歌手页面
        itemView.cardViewSinger.setOnClickListener {
            SingerActivity.withSinger(data[holder.adapterPosition], holder.itemView.imageViewSinger, ctx as Activity)
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(data[position]) {
            holder.itemView.textViewSingerName.text = name
            loadImg(imgUrl, holder.itemView.imageViewSinger)
        }
    }

    override fun getItemCount() = data.size

    private fun loadImg(url: String, imageView: ImageView) {
        if (App.isNoPic) {
            //无图模式
            imageView.setImageResource(R.drawable.ic_singer_gray)
        } else {
            Glide.with(ctx)
                    .load(url)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_singer_gray))
                    .into(imageView)
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


}