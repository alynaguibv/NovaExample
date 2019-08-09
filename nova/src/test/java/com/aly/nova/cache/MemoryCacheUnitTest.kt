package com.aly.nova.cache

import org.junit.Assert.assertEquals
import org.junit.Test

class MemoryCacheUnitTest {

    @Test
    fun cache_add() {
        val memoryCache = MemoryCache<Int, Int>()
        memoryCache.put(1, 1)
        memoryCache.put(2, 2)
        memoryCache.put(3, 3)
        assertEquals(3, memoryCache.cachedKeys().size)
    }

    @Test
    fun cache_get() {
        val memoryCache = MemoryCache<Int, String>()
        memoryCache.put(1, "Aly")
        memoryCache.put(2, "Mohammed")
        memoryCache.put(3, "Naguib")
        memoryCache.put(4, "Aly")
        memoryCache.put(5, "Abdellatif")
        memoryCache.put(6, "Aly")

        assertEquals("Naguib", memoryCache.get(3))
    }

    @Test
    fun cache_oversize() {
        val memoryCache = MemoryCache<Int, String>(5)
        memoryCache.put(1, "Aly")
        memoryCache.put(2, "Mohammed")
        memoryCache.put(3, "Naguib")
        memoryCache.put(4, "Aly")
        memoryCache.put(5, "Abdellatif")
        memoryCache.put(6, "Aly")

        assertEquals(5, memoryCache.cachedKeys().size)
    }

    @Test
    fun cache_clean() {
        val memoryCache = MemoryCache<Int, String>(15)

        assertEquals(0, memoryCache.cachedKeys().size)

        memoryCache.put(1, "A1")
        memoryCache.put(2, "A2")
        memoryCache.put(3, "A3")
        memoryCache.put(4, "A4")
        memoryCache.put(5, "A5")
        memoryCache.put(6, "A6")

        assertEquals(6, memoryCache.cachedKeys().size)

        memoryCache.clear()

        assertEquals(0, memoryCache.cachedKeys().size)
    }

}