package com.moshy.containers

/**
 * Like `a.zip(b) { a, b -> ... }` but doesn't produce a `List<Unit>`.
 *
 * Trivia: one could implement [Iterable.zip] with this via a construction like
 * ```
 *  fun <T, U> Iterable<T>.zip(other: Iterable<U>) =
 *      buildList { zipForEach(other) { a, b -> add(a to b) } }
 *  ```
 * @see [Iterable.zip]
 * @see forEach
 */

inline fun <T, U> Iterable<T>.zipForEach(other: Iterable<U>, action: (a: T, b: U) -> Unit) {
    val first = iterator()
    val second = other.iterator()
    while (first.hasNext() && second.hasNext())
        action(first.next(), second.next())
}
