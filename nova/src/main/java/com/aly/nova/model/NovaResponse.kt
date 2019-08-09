package com.aly.nova.model

internal class NovaResponse(var actual: ByteArray?, var throwable: Throwable?) {
    constructor() : this(null, null)
}