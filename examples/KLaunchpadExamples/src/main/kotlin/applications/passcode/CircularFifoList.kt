package com.harry1453.klaunchpad.examples.applications.passcode

class CircularFifoList<T: Any>(private val array: Array<T?>) : AbstractMutableList<T>() {
    override var size = 0

    override fun contains(element: T): Boolean {
        return array.contains(element)
    }

    private fun rangeCheck(index: Int) {
        if (index < 0 || index >= size) throw NoSuchElementException()
    }

    override fun get(index: Int): T {
        rangeCheck(index)
        return array[index]!!
    }

    override fun indexOf(element: T): Int {
        return array.indexOf(element)
    }

    override fun lastIndexOf(element: T): Int {
        return array.lastIndexOf(element)
    }

    override fun add(element: T): Boolean {
        if (size == array.size) { // We are full
            // Cycle array forward one
            array.copyInto(array, startIndex = 1, endIndex = size)
            array[size - 1] = element
        } else {
            // Insert in last position
            array[size] = element
            size++
        }
        return true
    }

    override fun add(index: Int, element: T) {
        rangeCheck(index)
    }

    override fun removeAt(index: Int): T {
        rangeCheck(index)
        val removed = array[index]!!
        array.copyInto(array, destinationOffset = index, startIndex = index + 1, endIndex = size)
        size--
        return removed
    }

    override fun set(index: Int, element: T): T {
        rangeCheck(index)
        val replaced = array[index]!!
        array[index] = element
        return replaced
    }

    companion object {
        inline fun <reified T: Any> new(maxLength: Int): CircularFifoList<T> {
            require(maxLength > 0) { "Max Length must be at least 1" }
            return CircularFifoList(arrayOfNulls(maxLength))
        }
    }
}
