package com.aly.nova.okhttp

import org.junit.Assert.assertNotNull
import org.junit.Test

class ConnectionManagerUnitTest {

    @Test
    fun assertMainResponse() {
        val loadManager = ConnectionManager()
        val byteArray = loadManager.loadFrom("http://pastebin.com/raw/wgkJgazE")
        try {
            assertNotNull(byteArray)
        } catch (exception: Exception) {
            assertNotNull(exception)
        }
    }

    @Test
    fun assertInvalidLink() {
        val loadManager = ConnectionManager()
        try {
            loadManager.loadFrom("invalid")
        } catch (exception: Exception) {
            assertNotNull(exception)
        }
    }

    @Test
    fun assertByteArrayToString() {
        val loadManager = ConnectionManager()
        val byteArray = loadManager.loadFrom("http://pastebin.com/raw/wgkJgazE")
        try {
            val resString = byteArray?.let { String(it) }
            assertNotNull(resString)
        } catch (exception: Exception) {
            assertNotNull(exception)
        }
    }
}