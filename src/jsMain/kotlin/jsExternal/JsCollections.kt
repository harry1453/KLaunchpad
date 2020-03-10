package jsExternal

external interface JsIteratorResult<T> {
    var done: Boolean
    var value: T
}

external interface JsIterator<T> {
    fun next(value: Any = definedExternally): JsIteratorResult<T>
}

external interface JsMap<K, V> {
    val size: Int
    fun keys(): JsIterator<K>
    fun values(): JsIterator<V>

    fun clear()
    fun delete(key: K): Boolean
    fun get(key: K): V?
    fun set(key: K, value: V)
}
