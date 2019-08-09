package com.aly.nova.okhttp

import com.squareup.okhttp.Call
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import java.io.IOException
import java.util.concurrent.TimeUnit

internal class ConnectionManager(timeout: Long?) {
    constructor() : this(null)

    private var client: OkHttpClient = OkHttpClient()
    private var defaultTimeout = 10000L
    private val callMap = HashMap<String, Call>()

    init {
        if (timeout == null) {
            setTimeout(defaultTimeout)
        } else {
            setTimeout(timeout)
            defaultTimeout = timeout
        }
    }

    private fun setTimeout(timeout: Long) {
        client.setConnectTimeout(timeout, TimeUnit.MILLISECONDS)
        client.setReadTimeout(timeout, TimeUnit.MILLISECONDS)
    }

    private fun getCall(url: String, headers: Map<String, String>?, timeout: Long?): Call {
        if (timeout != null && timeout != -1L) {
            setTimeout(timeout)
        }
        val request = Request.Builder().url(url).build()
        headers?.let {
            for (entry in headers)
                request.newBuilder().addHeader(entry.key, entry.value)
        }
        return client.newCall(request)
    }

    fun loadFrom(url: String, headers: Map<String, String>?, timeout: Long?): ByteArray? {

        if (!callMap.contains(url)) {
            callMap[url] = getCall(url, headers, timeout)
        }

        val call: Call? = callMap[url]

        if (call != null) {
            val response = call.execute()
            if (!response.isSuccessful) {
                setTimeout(defaultTimeout)
                throw IOException("Unexpected ".plus(response))
            } else {
                setTimeout(defaultTimeout)
                return response.body().bytes()
            }
        }
        return null
    }

    fun loadFrom(url: String, headers: Map<String, String>?): ByteArray? {
        return loadFrom(url, headers, null)
    }

    fun loadFrom(url: String): ByteArray? {
        return loadFrom(url, null, null)
    }

    fun loadFrom(url: String, timeout: Long?): ByteArray? {
        return loadFrom(url, null, timeout)
    }

    fun cancel(url: String) {
        callMap[url]?.cancel()
    }
}