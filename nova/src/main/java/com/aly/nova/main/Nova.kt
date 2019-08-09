package com.aly.nova.main

import com.aly.nova.cache.MemoryCache
import com.aly.nova.callback.DataCallback
import com.aly.nova.model.NovaConverter
import com.aly.nova.model.NovaResponse
import com.aly.nova.okhttp.ConnectionManager
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class Nova {

    companion object {
        private var memoryCache = MemoryCache<String, Observable<ByteArray>>()
        private val connectionManager = ConnectionManager()
        fun <T> from(url: String, using: NovaConverter<T>, dataCallback: DataCallback<T>) {
            val loader = Loader<T>(memoryCache, connectionManager)
            loader.load(url, using, dataCallback)
        }

        fun cancel(url: String) {
            Observable.fromCallable {
                connectionManager.cancel(url)
            }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe()
        }
    }

    private class Loader<T>(
        val memoryCache: MemoryCache<String, Observable<ByteArray>>,
        val connectionManager: ConnectionManager
    ) {

        private fun getNovaResponseObservable(url: String): Observable<NovaResponse> {
            return Observable.create {
                val novaResponse = NovaResponse()
                try {
                    val byteArray = connectionManager.loadFrom(url)
                    if (byteArray != null) {
                        novaResponse.actual = byteArray
                    }
                    it.onNext(novaResponse)
                    it.onComplete()
                } catch (throwable: Throwable) {
                    novaResponse.throwable = throwable
                    it.onError(throwable)
                }
            }
        }

        fun load(url: String, using: NovaConverter<T>, callback: DataCallback<T>) {
            val observable = memoryCache.get(url)
            if (observable == null) {
                val responseObservable =
                    getNovaResponseObservable(url).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                responseObservable.subscribe(object : Observer<NovaResponse> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: NovaResponse) {
                        handleNovaResponse(url, t, using, callback)
                    }

                    override fun onError(e: Throwable) {
                        val novaResponse = NovaResponse()
                        novaResponse.throwable = e
                        handleNovaResponse(url, novaResponse, using, callback)
                        e.printStackTrace()
                    }

                })
            } else {
                observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    object : Observer<ByteArray> {
                        override fun onComplete() {

                        }

                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(t: ByteArray) {
                            val novaResponse = NovaResponse()
                            novaResponse.actual = t
                            handleNovaResponse(url, novaResponse, using, callback)
                        }

                        override fun onError(e: Throwable) {
                            val novaResponse = NovaResponse()
                            novaResponse.throwable = e
                            handleNovaResponse(url, novaResponse, using, callback)
                            e.printStackTrace()
                        }
                    })
            }
        }

        fun handleNovaResponse(
            url: String,
            novaResponse: NovaResponse,
            using: NovaConverter<T>,
            callback: DataCallback<T>
        ) {
            novaResponse.actual?.let {
                val t = using.from(it)
                callback.onDataReceived(t)
                val observable = Observable.just(it)
                memoryCache.put(url, observable)
            }
            novaResponse.throwable?.let {
                callback.onError(it)
            }
        }

    }
}