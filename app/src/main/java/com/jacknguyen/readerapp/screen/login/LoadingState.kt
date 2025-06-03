package com.jacknguyen.readerapp.screen.login

import android.net.http.UrlRequest.Status

data class LoadingState(val status:Status,val message:String? = null) {
    companion object {
        val IDLE = LoadingState(Status.IDLE)
        val LOADING = LoadingState(Status.LOADING)
        val SUCCESS = LoadingState(Status.SUCCESS)
        val ERROR = LoadingState(Status.FAILED)
    }

    enum class Status {
        IDLE,
        LOADING,
        SUCCESS,
        FAILED
    }
}
