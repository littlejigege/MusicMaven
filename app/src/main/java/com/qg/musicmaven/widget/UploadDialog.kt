package com.qg.musicmaven.widget

import android.app.DialogFragment
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.mobile.utils.JsonMaker
import com.mobile.utils.onTextChange
import com.mobile.utils.value
import com.qg.musicmaven.App
import com.qg.musicmaven.R
import com.qg.musicmaven.modle.QiNiu
import com.qg.musicmaven.modle.bean.FeedBack
import com.qg.musicmaven.utils.Fetcher
import com.qg.musicmaven.utils.FilePicker
import com.qg.musicmaven.utils.SuggestionGetter
import com.qiniu.android.storage.UpCancellationSignal
import com.qiniu.android.storage.UpProgressHandler
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_upload.view.*
import okhttp3.MediaType
import okhttp3.RequestBody

/**
 * Created by jimiji on 2017/12/15.
 */
class UploadDialog : DialogFragment() {
    lateinit var rootView: View
    private var isUploading = false
    private var isFileSelected = false
    private val fetcher = Fetcher()
    private lateinit var editTextSinger: AutoCompleteTextView
    private lateinit var editTextSong: AutoCompleteTextView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        isCancelable = false
        rootView = inflater.inflate(R.layout.dialog_upload, container, false)
        //设置选择文件按钮
        rootView.textViewSelectFile.setOnClickListener {
            //上传过程不允许选择文件
            if (!isUploading) {
                val picker = FilePicker(activity, fragmentManager, FilePicker.PICK_AUDIO)
                picker.onFinish {
                    rootView.textViewSelectFile.text = it[0]
                    isFileSelected = true
                    updateBtYesEnable()
                }
                picker.show()
            }
        }
        //让textview可以上下滚动，防止文件路径过长无法显示完整
        rootView.textViewSelectFile.movementMethod = ScrollingMovementMethod.getInstance()
        editTextSinger = rootView.inputLayoutSingerUpload.editText as AutoCompleteTextView
        editTextSong = rootView.inputLayoutSongUpload.editText as AutoCompleteTextView
        editTextSinger.threshold = 1
        editTextSong.threshold = 1
        //监听两个输入框变化
        editTextSinger.onTextChange {
            SuggestionGetter.getSingerSuggestion(it) {
                println(it)
                editTextSinger.setAdapter(ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, it))
            }
            updateBtYesEnable()
        }
        editTextSong.onTextChange {
            SuggestionGetter.getMusicSuggestion(it) {
                editTextSong.setAdapter(ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, it))
            }
            updateBtYesEnable()
        }
        //设置下放两个按钮
        rootView.btYse.setOnClickListener {

            isUploading = true
            updateBtYesEnable()
            upload()
        }
        rootView.btCancel.setOnClickListener {
            isUploading = false
            dismiss()
        }
        return rootView
    }

    private fun updateBtYesEnable() {
        rootView.btYse.isEnabled = (!isUploading && isFileSelected && rootView.inputLayoutSingerUpload.editText!!.value.isNotEmpty()
                && rootView.inputLayoutSongUpload.editText!!.value.isNotEmpty())
    }

    override fun onResume() {
        val params = dialog.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = params as android.view.WindowManager.LayoutParams
        super.onResume()
    }

    private fun upload() {
        QiNiu.upLoad(rootView.textViewSelectFile.text.toString(), UpProgressHandler { key, percent ->
            updateProgress(key, percent)
        }, UpCancellationSignal {
            !isUploading
        })
    }

    private fun updateProgress(key: String, percent: Double) {
        //当进度100时，postsong到服务器
        if (percent == 1.0) {
            fetcher.fetchIO(App.serverApi.postSong(RequestBody.create(MediaType.parse("application/json"), JsonMaker.make {
                objects {
                    "playUrl" - "${App.QIQUI_ADDRESS}/$key"
                    "songName" - rootView.inputLayoutSongUpload.editText!!.value
                    "singerName" - rootView.inputLayoutSingerUpload.editText!!.value
                    "customerId" - if (App.instance.hasUser()) App.instance.getUser()!!.userId else "-1"
                }
            })))
            dismiss()
            return
        }
        //更新进度显示
        rootView.textViewProgressLeft.text = "${(percent * 100).toInt()}%"
        rootView.textViewProgressRight.text = "${(percent * 100).toInt()}/100"
        rootView.progressBarUpload.progress = (percent * 100).toInt()
    }

}