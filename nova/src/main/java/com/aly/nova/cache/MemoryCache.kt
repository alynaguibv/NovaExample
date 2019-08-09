package com.aly.nova.cache


class MemoryCache<K, V>(size: Int) {

    private var first: Entry<K, V>? = null
    private var last: Entry<K, V>? = null
    private var size: Int = size
    private var mapEntries: HashMap<K, Entry<K, V>> = HashMap(size)

    constructor() : this(10)

    private class Entry<K, V> {
        internal var prev: Entry<K, V>? = null
        internal var next: Entry<K, V>? = null
        internal var key: K? = null
        internal var value: V? = null
    }

    fun get(key: K): V? {
        if (mapEntries.contains(key)) {
            val mapEntry = mapEntries[key]
            if (mapEntry != null) {
                removeEntry(mapEntry)
                moveItemUp(mapEntry)
                return mapEntry.value
            }
        }
        return null
    }

    fun put(key: K, value: V) {
        if (mapEntries.contains(key)) {
            val entry = mapEntries[key]
            entry?.value = value
            if (entry != null) {
                remove(entry)
            }
            if (entry != null) {
                moveItemUp(entry)
            }
        } else {
            val entry = Entry<K, V>()
            entry.key = key
            entry.value = value
            if (mapEntries.size == size) {
                mapEntries.remove(last?.key)
                last?.let { remove(it) }
            }
            moveItemUp(entry)
            mapEntries[key] = entry
        }
    }

    private fun removeEntry(mapEntry: Entry<K, V>) {
        if (mapEntry.prev != null) {
            mapEntry.prev?.next = mapEntry.next
        } else {
            first = mapEntry
        }
        if (mapEntry.next != null) {
            mapEntry.next?.prev = mapEntry.prev
        } else {
            last = mapEntry
        }

    }

    private fun remove(mapEntry: Entry<K, V>) {
        if (mapEntry.prev != null) {
            mapEntry.prev?.next = mapEntry.next
        } else {
            first = mapEntry.next
        }

        if (mapEntry.next != null) {
            mapEntry.next?.prev = mapEntry.prev
        } else {
            last = mapEntry.prev
        }
    }

    private fun moveItemUp(mapEntry: Entry<K, V>) {
        mapEntry.next = first

        first?.prev = mapEntry

        first = mapEntry
        if (last == null) {
            last = first
        }
    }

    override fun toString(): String {
        val sb = StringBuilder("{")
        var delimiter = ""
        for (entry in cachedValues()) {
            sb.append(delimiter).append(entry.key).append("=").append(entry.value)
            delimiter = ", "
        }
        sb.append("}")
        return sb.toString()
    }

    fun cachedKeys(): Set<K> {
        return HashSet(mapEntries.keys)
    }

    fun clear() {
        mapEntries.clear()
        first?.next = null
        first?.prev = null
        last?.next = null
        last?.prev = null
        first = null
        last = null
    }

    private fun cachedValues(): Set<Entry<K, V>> {
        return HashSet(mapEntries.values)
    }
}