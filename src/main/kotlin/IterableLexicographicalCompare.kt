package com.moshy.containers

/**
 * Lexicographically compare two Iterables.
 *
 * Behavior is undefined if the sequences aren't ordered (eg `Set<E: Comparable<E>`).
 *
 */

fun <T : Comparable<T>> Iterable<T>.lexicographicallyCompareTo(other: Iterable<T>) =
    lexicographicallyCompareTo(other) { a, b -> a.compareTo(b) }

/** @see lexicographicallyCompareTo */
operator fun <T : Comparable<T>> Iterable<T>.compareTo(other: Iterable<T>): Int =
    lexicographicallyCompareTo(other)

fun <T> Iterable<T>.lexicographicallyCompareTo(other: Iterable<T>, c: Comparator<T>): Int {
    val first = iterator()
    val second = other.iterator()
    while (first.hasNext() && second.hasNext()) {
        val cmp = c.compare(first.next(), second.next())
        if (cmp != 0)
            return cmp
    }
    if (first.hasNext())
        return -1
    if (second.hasNext())
        return 1
    return 0
}
