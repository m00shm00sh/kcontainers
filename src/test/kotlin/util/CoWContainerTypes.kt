package com.moshy.containers.util

import com.moshy.containers.CopyOnWriteContainer


/*
 * Tests copy-on-write behavior, basic container operation forwarding (equals, hashCode, toString), helper functions.
 */

/* Dummy container for tracing writes. We implement MutableCollection<> enough to satisfy the type
 * contract, but absolutely not enough to be general-purpose usable.
 */
internal class TracingMutableCollectionOfObjects(): MutableCollection<Any> {
    var modCount: Int = 0
        private set

    /* is copy-constructible */
    constructor(unused: Collection<Any>): this()

    /* implements Collection<> */
    override val size: Int = 0
    override fun contains(element: Any) = unsupported()
    override fun containsAll(elements: Collection<Any>) = unsupported()
    override fun isEmpty() = unsupported()

    /* is container */
    override fun equals(other: Any?) = this === other || other is TracingMutableCollectionOfObjects
    // no data is stored, so hashCode will never change
    override fun hashCode() = 0
    override fun toString(): String = STR

    /* To simulate the iterator-over-immutable-snapshot property, we use a dummy Iterator
     * that produces an infinite sequence of a singleton prior to modification.
     */
    override fun iterator(): MutableIterator<Any> = object : MutableIterator<Any> {
        val modified = modCount == 0
        override fun hasNext(): Boolean = !modified
        override fun next(): Any = object {}
        override fun remove() = unsupported()
    }
    /* implements MutableCollection<> */
    override fun add(element: Any) = unsupported()
    override fun addAll(elements: Collection<Any>) = unsupported()
    override fun clear() { ++modCount }
    override fun remove(element: Any) = unsupported()
    override fun removeAll(elements: Collection<Any>) = unsupported()
    override fun retainAll(elements: Collection<Any>) = unsupported()

    companion object {
        private fun unsupported(vararg unused: Any?): Nothing = throw UnsupportedOperationException()
        const val STR = "TRACER"
    }

}

internal typealias TracerContainer = CopyOnWriteContainer<Collection<Any>, TracingMutableCollectionOfObjects>

// Honestly, the only use case I see for implementing CopyOnWriteCollection
internal class CoWTracer(init: Collection<Any> = TracingMutableCollectionOfObjects())
    : TracerContainer(init, ::TracingMutableCollectionOfObjects), Collection<Any>
{
    // skeletal Collection<>
    override val size: Int
        get() = data.size
    override fun contains(element: Any) = data.contains(element)
    override fun containsAll(elements: Collection<Any>) = data.containsAll(elements)
    override fun isEmpty() = data.isEmpty()
    override fun iterator() = data.iterator()
}