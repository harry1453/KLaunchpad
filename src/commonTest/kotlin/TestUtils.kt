import kotlin.test.assertEquals

fun assertArrayEquals(expected: ByteArray, actual: ByteArray) {
    return assertArrayEquals(expected.toTypedArray(), actual.toTypedArray())
}

fun <T> assertArrayEquals(expected: Array<T>, actual: Array<T>) {
    assertEquals(expected.size, actual.size)
    expected.forEachIndexed {index, element ->
        assertEquals(element, actual[index], "Arrays differed at index $index")
    }
}
