package com.qg.musicmaven.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.qg.musicmaven.App
import com.qg.musicmaven.R
import com.qg.musicmaven.modle.Audio
import com.qg.musicmaven.modle.AudioInfo
import com.qg.musicmaven.modle.FeedBack
import com.qg.musicmaven.modle.ServerAudio
import com.qg.musicmaven.netWork.KuGouApi
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import kotlinx.android.synthetic.main.audio_item.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * Created by jimji on 2017/9/9.
 */
class ServerAudioAdapter(var data: MutableList<ServerAudio>, var ctx: Context) : RecyclerView.Adapter<ServerAudioAdapter.ViewHolder>() {

    override fun getItemCount(): Int = data.size
    private var _onItemClick: (ServerAudio) -> Unit = {}

    fun onItemClick(o: (ServerAudio) -> Unit) {
        _onItemClick = o
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val audioInfo = data[position]
        with(audioInfo) {
            holder.itemView.audioName.text = songName.replace("<em>", "").replace("</em>", "")
            holder.itemView.singerName.text = singer
            loadImg(audioInfo.imgUrl!!, holder.itemView.imageView)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.audio_item, parent, false)
        return ViewHolder(itemView)
    }

    private fun loadImg(url: String, imageView: ImageView) {
        Glide.with(ctx)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_music))
                .into(imageView)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.onClick { _onItemClick(data[adapterPosition]) }
        }
    }
}