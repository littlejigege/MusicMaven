package com.qg.musicmaven.modle.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by jimiji on 2017/12/14.
 */
data class Singer(@SerializedName("singerName") var name: String, var imgUrl: String = "") : Serializable
