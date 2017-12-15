package com.qg.musicmaven.modle.bean

import java.io.Serializable


/**
 * Created by jimiji on 2017/12/14.
 */
data class User(var userEmail: String = "", var customerName: String = "",
                var password: String = "", var customerId: Int, var faceId: String? = null):Serializable