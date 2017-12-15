package com.qg.musicmaven.modle.bean

/**
 * Created by steve on 17-12-15.
 */
data class RegisterBody(val method:String , val data : Data){

    data class Data(val customerName : String ,val userEmail : String , val password : String , val registerCount :String , val faceId :String)
}