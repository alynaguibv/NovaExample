package com.aly.nova.callback

interface DataCallback<T> {

    fun onDataReceived(t: T)

    fun onError(throwable: Throwable)

}