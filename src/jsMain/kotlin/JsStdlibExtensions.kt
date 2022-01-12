import jsExternal.JsMap

internal fun <K, V> JsMap<K, V>.toMap(): Map<K, V> {
    return object : AbstractMap<K, V>() {
        override val entries = object : AbstractSet<Map.Entry<K, V>>() {
            override val size: Int
                get() = this@toMap.size

            override fun iterator(): Iterator<Map.Entry<K, V>> {
                val keyIterator = this@toMap.keys()
                val valueIterator = this@toMap.values()

                return object : AbstractIterator<Map.Entry<K, V>>() {
                    override fun computeNext() {
                        val nextKey = keyIterator.next()
                        val nextValue = valueIterator.next()
                        if (nextKey.done == true || nextValue.done == true) {
                            done()
                            return
                        }

                        setNext(object : Map.Entry<K, V> {
                            override val key = nextKey.value
                            override val value = nextValue.value
                        })
                    }
                }
            }
        }
    }
}
