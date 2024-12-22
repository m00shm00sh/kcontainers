package com.moshy.containers.cowcoro.util

import com.moshy.containers.coroutines.CopyOnWriteContainer
import com.moshy.containers.util.TracingMutableCollectionOfObjects

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