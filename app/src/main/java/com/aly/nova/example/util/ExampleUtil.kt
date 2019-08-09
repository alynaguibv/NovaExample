package com.aly.nova.example.util

import com.aly.nova.example.model.ExampleResponse
import com.aly.nova.example.model.PasteBin
import com.aly.nova.model.NovaConverter
import com.google.gson.Gson
import com.google.gson.JsonArray

class ExampleUtil {
    companion object {
        val pastePinConverter = object : NovaConverter<ExampleResponse> {
            val gson = Gson()
            override fun from(byteArray: ByteArray): ExampleResponse {
                val string = String(byteArray)
                val arr = gson.fromJson(string, JsonArray::class.java)
                val list = ArrayList<PasteBin>()
                for (json in arr) {
                    val pp = gson.fromJson(json, PasteBin::class.java)
                    list.add(pp)
                }
                return ExampleResponse(list)
            }
        }
    }
}