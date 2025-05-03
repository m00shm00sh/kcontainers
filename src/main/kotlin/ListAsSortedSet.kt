package com.moshy.containers

import kotlin.math.log2
import kotlin.math.roundToInt

typealias SortedSet<T> = ListAsSortedSet<T>

/** A sorted list with distinct elements. Behavior is undefined if list is not already sorted and distinct. */
open class ListAsSortedSet<T> internal constructor(
    open val list: List<T>,
    internal val lowerLimit: T? = null,
    internal val upperLimit: T? = null,
    val comparator: Comparator<T>,
): AbstractCollection<T>(), Set<T> {
    override fun contains(element: T): Boolean = indexOf(element) >= 0
    override fun containsAll(elements: Collection<T>): Boolean =
        iterationEquals(doIntersect(elements), elements)

    override val size: Int
        get() = list.size

    override fun isEmpty(): Boolean = size == 0

    override fun iterator(): Iterator<T> = list.iterator()

    override fun equals(other: Any?): Boolean =
        (this === other) ||
        ((other as? ListAsSortedSet<*>)?.let { list == it.list } == true) ||
        ((other as? Collection<*>)?.let { iterationEquals(this, it) } == true)

    override fun hashCode(): Int = list.hashCode()

    // from java.util.SortedSet<T> and kotlinized
    fun first(): T = list.first()
    fun firstOrNull(): T? = list.firstOrNull()
    fun last(): T = list.last()
    fun lastOrNull(): T? = list.lastOrNull()

    /** Returns a view of the portion of this set whose elements range from [fromElement], inclusive,
     *  to [toElement], exclusive.
     */
    fun subSet(fromElement: T, toElement: T): ListAsSortedSet<T> {
        checkRange(fromElement, -1, -1)
        checkRange(toElement, 0, 1)
        require(comparator.compare(fromElement, toElement) <= 0) {
            "from=$fromElement > to=$toElement"
        }
        var fromIndex = indexOf(fromElement)
        if (fromIndex < 0)
            fromIndex = -(fromIndex + 1)
        var toIndex = indexOf(toElement)
        if (toIndex < 0)
            toIndex = -(toIndex + 1)
        return ListAsSortedSet(list.subList(fromIndex, toIndex), fromElement, toElement, comparator)
    }
    /** Returns a view of the portion of this set whose elements are strictly less than [toElement]. */
    fun headSet(toElement: T): ListAsSortedSet<T> {
        checkRange(toElement, 0, 1)
        var at = indexOf(toElement)
        if (at < 0)
            at = -(at + 1)
        return ListAsSortedSet(list.subList(0, at), comparator = comparator, upperLimit = toElement)
    }
    /** Returns a view of the portion of this set whose elements are greater than or equal to [fromElement]. */
    fun tailSet(fromElement: T): ListAsSortedSet<T> {
        checkRange(fromElement, -1, -1)
        var at = indexOf(fromElement)
        if (at < 0)
            at = -(at + 1)
        return ListAsSortedSet(list.subList(at, size), comparator = comparator, lowerLimit = fromElement)
    }

    protected fun indexOf(element: T) =
        list.binarySearch(element, comparator)

    protected fun doIntersect(elements: Collection<T>) =
            if (elements is ListAsSortedSet<T>)
                intersect(elements)
            else
                intersect(elements.copyToSortedSet(comparator))

    /* dir <=0 -> check lower range; inc < 0 -> inclusive of eq
     * dir >= 0 -> check upper range; inc > 0 -> inclusive of eq
     * bidirectionally closed set not supported
     */
    protected fun checkRange(element: T, inc: Int, dir: Int = 0) {
        if (dir < 1)
            lowerLimit?.let {
                val cmp = comparator.compare(lowerLimit, element)
                fun eMsg(c: String) = { "value too low (e=$element $c $lowerLimit)" }
                if (inc > -1)
                    require(cmp < 0, eMsg("<"))
                else
                    require(cmp <= 0, eMsg("<="))
            }
        if (dir > -1)
            upperLimit?.let {
                val cmp = comparator.compare(element, upperLimit)
                fun eMsg(c: String) = { "value too high (e=$element $c $upperLimit)" }
                if (inc < 1)
                    require(cmp < 0, eMsg("<"))
                else
                    require(cmp <= 0, eMsg("<="))
            }
    }
}

/** Transforms a sorted (by natural comparison) and distinct list into a set-like object. */
fun <T: Comparable<T>> List<T>.assertIsSortedSet(): ListAsSortedSet<T> =
    ListAsSortedSet(this) { a, b -> a.compareTo(b) }
/** Transforms a sorted and distinct list into a set-like object. */
fun <T> List<T>.assertIsSortedSet(c: Comparator<T>): ListAsSortedSet<T> =
    ListAsSortedSet(this, comparator = c)

/** Converts a collection to a sorted set by natural comparison. */
fun <T: Comparable<T>> Collection<T>.copyToSortedSet(): ListAsSortedSet<T> {
    val distinct = toSet()
    val sorted = buildList(distinct.size) {
        addAll(distinct)
        sort()
    }
    return sorted.assertIsSortedSet()
}
/** Converts a collection to a sorted set. */
fun <T> Collection<T>.copyToSortedSet(c: Comparator<T>): ListAsSortedSet<T> {
    val distinct = toSet()
    val sorted = buildList(distinct.size) {
        addAll(distinct)
        sortWith(c)
    }
    return sorted.assertIsSortedSet(c)
    }

fun <T> ListAsSortedSet<T>.intersect(other: ListAsSortedSet<T>): ListAsSortedSet<T> {
    val larger = if (size > other.size) this else other
    val smaller = if (size < other.size) this else other
    val result =
        if (smaller.size + larger.size > smaller.size * log2(larger.size.toDouble()).roundToInt())
            smaller.intersectBinarySearch(larger)
        else
            this.intersectLinearScan(other)
    return result.assertIsSortedSet(comparator)
}

private fun <T> Collection<T>.intersectBinarySearch(other: ListAsSortedSet<T>): List<T> =
    filter(other::contains)

// derived from https://stackoverflow.com/a/7164616
private fun <T> ListAsSortedSet<T>.intersectLinearScan(other: ListAsSortedSet<T>): List<T> {
    val thisCmp = comparator
    val thisItr = iterator()
    val otherItr = other.iterator()
    if (!thisItr.hasNext() || !otherItr.hasNext())
        return emptyList()
    var a = thisItr.next()
    var b = otherItr.next()
    return buildList {
        while (true) {
            val cmp = thisCmp.compare(a, b)
            when {
                cmp < 0 -> {
                    if (!thisItr.hasNext())
                        return@buildList
                    a = thisItr.next()
                }
                cmp > 0 -> {
                    if (!otherItr.hasNext())
                        return@buildList
                    b = otherItr.next()
                }
                else -> {
                    add(a)
                    if (!thisItr.hasNext() || !otherItr.hasNext())
                        return@buildList
                    a = thisItr.next()
                    b = otherItr.next()
                }
            }
        }
    }
}

private fun <T> iterationEquals(c1: Collection<T>, c2: Collection<T>): Boolean {
    if (c1.size != c2.size)
        return false
    val c1Itr = c1.iterator()
    val c2Itr = c2.iterator()
    while (c1Itr.hasNext() && c2Itr.hasNext())
        if (c1Itr.next() != c2Itr.next())
            return false
    return true
}

/** Builds a copy preserving the sorted set property.
 *
 *  Calls to [MutableSet.add] and [MutableSet.remove] should be minimized because each operation carries a
 *  O(N) performance cost.
 */
fun <T> ListAsSortedSet<T>.buildCopy(builder: MutableSet<T>.() -> Unit): ListAsSortedSet<T> =
    LaSSBuilder(list.toMutableList(), lowerLimit, upperLimit, comparator).apply(builder)

private class LaSSBuilder<T>(
    override val list: MutableList<T>,
    lowerLimit: T?,
    upperLimit: T?,
    comparator: Comparator<T>,
): ListAsSortedSet<T>(list, lowerLimit, upperLimit, comparator), MutableSet<T> {
    override fun iterator(): MutableIterator<T> = list.iterator()

    override fun add(element: T): Boolean {
        checkRange(element, -1, 0)
        val index = indexOf(element)
        if (index >= 0)
            return false
        list.add(-(index + 1), element)
        return true
    }

    override fun addAll(elements: Collection<T>): Boolean {
        /* TODO: investigate at what sizes of list and elements there is a point to converting elements
         *       to LaSS to minimize copy overhead arising from multiple insertions
         */
        var added = false
        for (e in elements) {
            added = added or add(e)
        }
        return added
    }

    override fun remove(element: T): Boolean {
        val index = indexOf(element)
        if (index < 0)
            return false
        list.removeAt(index)
        return true
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        var removed = false
        /* reverse elements to minimize multiple copy overhead;
         * same caveat as with addAll() applies
         */
        for (e in elements.reversed()) {
            removed = removed or remove(e)
        }
        return removed
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val intersection = doIntersect(elements)
        val changed = this.list != intersection.list
        list.clear()
        list.addAll(intersection)
        return changed
    }

    override fun clear() {
        list.clear()
    }
}
