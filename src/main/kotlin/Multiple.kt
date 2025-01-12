package com.moshy.containers

/**
 * Returns multiple elements matching an optionally supplied [predicate],
 * or throws an exception if the collection has the wrong number of elements.
 *
 * When [num] is 1, this behaves like `listOf(Iterable<T>.single())`.
 */
fun <T> Iterable<T>.multiple(num: Int, predicate: (T) -> Boolean = { true }): List<T> =
    when {
        num < 0 -> throw IllegalArgumentException("num is negative")
        this is List -> this.multiple(num)
        else -> {
            buildList(num) {
                var remaining = num
                for (element in this@multiple) {
                    if (!predicate(element))
                        continue
                    if (remaining <= 0)
                        throw IllegalArgumentException("Collection has too many elements")
                    add(element)
                    --remaining
                }
                if (remaining > 0)
                    throw NoSuchElementException("Collection has missing elements")
            }
        }
    }

/**
 * Returns multiple elements, or throws an exception if the list has the wrong number of elements.
 *
 * When [num] is 1, this behaves like `listOf(List<T>.single())`.
 */
fun <T> List<T>.multiple(num: Int): List<T> =
    when {
        num < 0 -> throw IllegalArgumentException("num is negative")
        size < num -> throw NoSuchElementException("Collection has ${num - size} missing elements")
        size > num -> throw IllegalArgumentException("Collection has ${size - num} excess elements")
        else -> this
    }

/**
 * Returns multiple elements matching an optionally supplied [predicate],
 * or `null` if the collection has the wrong number of elements.
 *
 * When [num] is 1, this behaves like `listOf(Iterable<T>.single())`.
 * When [predicate] is specified, this behaves like `listOf(
 */
fun <T> Iterable<T>.multipleOrNull(num: Int, predicate: (T) -> Boolean = { true }): List<T>? =
    when {
        num < 0 -> throw IllegalArgumentException("num is negative")
        this is List -> this.multipleOrNull(num)
        else -> {
            buildList(num) {
                var remaining = num
                for (element in this@multipleOrNull) {
                    if (!predicate(element))
                        continue
                    if (remaining <= 0)
                        return null
                    add(element)
                    --remaining
                }
                if (remaining > 0)
                    return null
            }
        }
    }

/**
 * Returns multiple elements, or `null` if the list has the wrong number of elements.
 *
 * When [num] is 1, this behaves like `listOf(List<T>.single())`.
 */
fun <T> List<T>.multipleOrNull(num: Int): List<T>? =
    when {
        num < 0 -> throw IllegalArgumentException("num is negative")
        size != num -> null
        else -> this
    }
