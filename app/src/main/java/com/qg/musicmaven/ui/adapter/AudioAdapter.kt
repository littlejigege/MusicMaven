package com.qg.musicmaven.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.qg.musicmaven.App
import com.qg.musicmaven.R
import com.qg.musicmaven.modle.Audio
import com.qg.musicmaven.modle.AudioInfo
import com.qg.musicmaven.modle.FeedBack
import com.qg.musicmaven.netWork.KuGouApi
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import kotlinx.android.synthetic.main.audio_item.view.*
import org.jetbrains.anko.find

/**
 * Created by jimji on 2017/9/9.
 */
class AudioAdapter(var data: MutableList<AudioInfo>, var ctx: Context) : RecyclerView.Adapter<AudioAdapter.ViewHolder>() {

    override fun getItemCount(): Int = data.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val audioInfo = data[position]
        with(audioInfo) {
            holder.itemView.audioName.text = songName.replace("<em>", "").replace("</em>", "")
            holder.itemView.singerName.text = singerName
            App.retrofit.create(KuGouApi::class.java).getAudio(fileHash)
                    .subscribeOn(IoScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<FeedBack<Audio>?> {
                        override fun onComplete() {
                        }

                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(t: FeedBack<Audio>) {
                            Glide.with(ctx)
                                    .load(t.data.imgUrl)
                                    .placeholder(R.drawable.ic_music)
                                    .into(holder.itemView.imageView)
                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                        }
                    })
        }
    }

    override fun onViewRecycled(holder: ViewHolder?) {
        super.onViewRecycled(holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.audio_item, parent, false)
        return ViewHolder(itemView)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}