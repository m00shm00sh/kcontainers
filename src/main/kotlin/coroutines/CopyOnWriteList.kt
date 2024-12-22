package com.moshy.containers.coroutines

/**
 * Copy on write [List] that resembles [java.util.concurrent.CopyOnWriteArrayList] in that reads are unlocked but writes
 * are suspending. All writes shall go through [write].
 *
 * *Note*: `subList` returns a view of the current snapshot, much like `iterator`. All mutations count as structural.
 */
open class CopyOnWriteList<E>
protected constructor(
    initialData: List<E>,
    copier: (List<E>) -> MutableList<E>
): CopyOnWriteContainer<List<E>, MutableList<E>>(initialData, copier), List<E> {
    override val size: Int
        get() = data.size

    override fun contains(element: E) = data.contains(element)
    override fun containsAll(elements: Collection<E>) = data.containsAll(elements)
    override fun get(index: Int): E = data[index]
    override fun indexOf(element: E) = data.indexOf(element)
    override fun lastIndexOf(element: E) = data.lastIndexOf(element)
    override fun isEmpty() = data.isEmpty()
    override fun iterator() = data.iterator()
    override fun listIterator() = data.listIterator()
    override fun listIterator(index: Int) = data.listIterator(index)
    override fun subList(fromIndex: Int, toIndex: Int): List<E> = data.subList(fromIndex, toIndex)
}
