package com.moshy.containers

/**
 * Create a SubList, bounded on one side from the left. Returns null if index is invalid.
 * @see List.subList
 */
fun <T> List<T>.subListFromOrNull(fromIndex: Int): List<T>? {
    val toIndex = size
    if (fromIndex < 0 || fromIndex > toIndex) {
        return null
    }
    return subList(fromIndex, toIndex)
}
/**
 * Create a SubList, bounded on one side from the left.
 * @throws IndexOutOfBoundsException if index is invalid
 * @see List.subList
 */
fun <T> List<T>.subListFrom(fromIndex: Int) =
    subListFromOrNull(fromIndex) ?: throw IndexOutOfBoundsException("fromIndex: $fromIndex, size: $size")

/**
 * Create a SubList, bounded on one side from the right. Returns null if index is invalid.
 * @see List.subList
 */
fun <T> List<T>.subListToOrNull(toIndex: Int): List<T>? {
    if (toIndex < 0 || toIndex > size) {
        return null
    }
    return subList(0, toIndex)
}
/**
 * Create a SubList, bounded on one side from the right.
 * @throws IndexOutOfBoundsException if index is invalid
 * @see List.subList
 */
fun <T> List<T>.subListTo(toIndex: Int) =
    subListToOrNull(toIndex) ?: throw IndexOutOfBoundsException("toIndex: $toIndex, size: $size")