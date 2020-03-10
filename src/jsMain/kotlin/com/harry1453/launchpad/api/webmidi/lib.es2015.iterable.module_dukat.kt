@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS", "EXTERNAL_DELEGATION")

package com.harry1453.launchpad.api.webmidi

external interface IteratorResult<T> {
    var done: Boolean
    var value: T?
}

external interface TsStdLib_Iterator<T> {
    fun next(value: Any = definedExternally): IteratorResult<T>
    val `return`: ((value: Any) -> IteratorResult<T>)?
        get() = definedExternally
    val `throw`: ((e: Any) -> IteratorResult<T>)?
        get() = definedExternally
}

external interface IterableIterator<T> : TsStdLib_Iterator<T>

fun <T> IterableIterator<T>.toIterable(): Iterable<T> {
    return Iterable { this.getIterator() }
}

fun <T> IterableIterator<T>.getIterator(): Iterator<T> {
    return object : Iterator<T> {
        private var next: IteratorResult<T> = this@getIterator.next()
        private var nextUsed: Boolean = false

        private fun refreshNext() {
            if (nextUsed) {
                next = this@getIterator.next()
                nextUsed = false
            }
        }

        override fun hasNext(): Boolean {
            refreshNext()
            return !next.done
        }

        override fun next(): T {
            refreshNext()
            nextUsed = true
            return next.value ?: error("No value available for next()")
        }
    }
}
