package jsExternal

public external interface JsIteratorResult<T> {
    public var done: Boolean?
    public var value: T
}

public external interface JsIterator<T> {
    public fun next(value: Any = definedExternally): JsIteratorResult<T>
}

public external interface JsMap<K, V> {
    public val size: Int
    public fun keys(): JsIterator<K>
    public fun values(): JsIterator<V>

    public fun clear()
    public fun delete(key: K): Boolean
    public fun get(key: K): V?
    public fun set(key: K, value: V)
}
