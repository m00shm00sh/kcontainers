package com.moshy.containers

/**
 * Like `a.zip(b) { a, b -> ... }` but doesn't produce a `List<Unit>`.
 * @see zip
 * @see forEach
 */

inline fun <T, U> Iterable<T>.zipForEach(other: Iterable<U>, transform: (a: T, b: U) -> Unit) {
    val first = iterator()
    val second = other.iterator()
    while (first.hasNext() && second.hasNext())
        transform(first.next(), second.next())
}
