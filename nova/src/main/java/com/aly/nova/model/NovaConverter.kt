package com.aly.nova.model

interface NovaConverter<ToType> {

    fun from(byteArray: ByteArray): ToType

}