package com.moshy.containers

/**
 * Transposes a list of lists and applies a [transform] on the transpose.
 */

inline fun <T, R> List<List<T>>.transpose(transform: (l: List<T>) -> R): List<R> {
    if (isEmpty())
        return emptyList()
    val outerSize = size
    val innerSize = this[0].size
    for ((i, subList) in this.withIndex()) {
        require(subList.size == innerSize) {
            "subList $i has unequal size: ${subList.size}"
        }
    }
    val buf = MutableList<T?>(outerSize) { null }
    @Suppress("UNCHECKED_CAST") val newList = List(innerSize) {
        for (i in 0..< outerSize)
            buf[i] = this[i][it]
        transform(buf as List<T>)
    }
    return newList
}

/**
 * Transposes a list of lists.
 */
fun <T> List<List<T>>.transpose(): List<List<T>> =
    transpose { it.toList() /* force copy */ }