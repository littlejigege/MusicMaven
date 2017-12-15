package com.qg.musicmaven.modle.bean

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by steve on 17-12-12.
 */
data class VerifyResult(@SerializedName("ifv_result")val ifv_result : Result){

    data class Result(val candidates : List<Candidate> )
    data class Candidate(@SerializedName("model_id")val id:String, val decision : String, val score : Double, @SerializedName("user")val  uuid : String )
}