package com.qg.musicmaven.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.qg.musicmaven.App
import com.qg.musicmaven.R
import com.qg.musicmaven.modle.bean.Audio
import com.qg.musicmaven.modle.bean.AudioInfo
import com.qg.musicmaven.modle.bean.FeedBack
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import kotlinx.android.synthetic.main.audio_item.view.*


/**
 * Created by jimji on 2017/9/9.
 */
class AudioAdapter(var data: MutableList<AudioInfo>, var ctx: Context) : RecyclerView.Adapter<AudioAdapter.ViewHolder>() {

    override fun getItemCount(): Int = data.size
    private var _onItemClick: (AudioInfo) -> Unit = {}

    fun onItemClick(o: (AudioInfo) -> Unit) {
        _onItemClick = o
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val audioInfo = data[position]

        with(audioInfo) {
            holder.itemView.audioName.text = songName.replace("<em>", "").replace("</em>", "")
            holder.itemView.singerName.text = singerName.replace("<em>", "").replace("</em>", "")
            if (audioInfo.imgUrl != null) {
                loadImg(audioInfo.imgUrl!!, holder.itemView.imageView)
            } else {
                App.kugouApi.getAudio(fileHash, albumId)
                        .subscribeOn(IoScheduler())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<FeedBack<Audio>> {
                            override fun onComplete() {
                            }

                            override fun onSubscribe(d: Disposable) {
                                holder.dispose = d
                            }

                            override fun onNext(t: FeedBack<Audio>) {
                                audioInfo.imgUrl = t.data.imgUrl
                                loadImg(t.data.imgUrl, holder.itemView.imageView)
                            }

                            override fun onError(e: Throwable) {
                                e.printStackTrace()
                            }
                        })
            }
        }

    }

    override fun onViewRecycled(holder: ViewHolder) {
        if (holder.dispose != null) {
            holder.dispose!!.dispose()
            holder.dispose = null
        }
        super.onViewRecycled(holder)
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
        var dispose: Disposable? = null

        init {
            itemView.setOnClickListener { _onItemClick(data[adapterPosition]) }
        }
    }
}