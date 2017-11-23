package com.qg.musicmaven.modle

/**
 * Created by jimji on 2017/9/17.
 */
enum class Status(val code: Int) {
    OK(1),
    USER_ERROR(110),
    ACCOUNT_NOT_EXIST(250),
    PASSWORD_ERROR(300),
    ACCOUNT_ALREADY_EXIST(400),
    EMAIL_COUNT_ERROR(450),
    EMAIL_ERROR(600),
    AUTH_NULL(650),
    URL_NULL(700),
    UPLOAD_CLOUD_ERROR(800),
    ALREADY_LATEST(780)
}