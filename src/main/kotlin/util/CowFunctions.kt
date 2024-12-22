package com.moshy.containers.util

import com.moshy.containers.CopyOnWriteContainer as CoWContainer
import com.moshy.containers.coroutines.CopyOnWriteContainer as CoWCoroutinesContainer

/*
 * Support functions shared by both lock-based and coroutine-based CopyOnWriteContainer.
 *
 * General pattern:
 * private fun doXxxImpl(...) { ... }
 * @Suppress("NOTHING_TO_INLINE", "UnusedReceiverParameter")
 * internal inline fun <T: Any> CoWContainer<T, *>.doXxx(...) = doXxxImpl(...)
 * @Suppress("NOTHING_TO_INLINE", "UnusedReceiverParameter")
 * internal inline fun <T: Any> CoWCoroutinesContainer<T, *>.doXxx(...) = doXxxImpl(...)
 *
 */

/* We can't constrain ContainerT to be a subclass of either Map or Collection inside
 * the generic specification, so constrain it here and have it try to enforce that at
 * run-time.
 */
private fun constrainInitialDataImpl(data: Any) {
    require(
        when (data) {
            is Collection<*>, is Map<*, *> -> true
            else -> false
        }
    ) {
        "This container is not designed to be used with objects that are neither " +
                "Map<>s nor Collection<>s"
    }
}
@Suppress("NOTHING_TO_INLINE", "UnusedReceiverParameter")
internal inline fun <T: Any> CoWContainer<T, *>.constrainInitialData(obj: T) = constrainInitialDataImpl(obj)
@Suppress("NOTHING_TO_INLINE", "UnusedReceiverParameter")
internal inline fun <T: Any> CoWCoroutinesContainer<T, *>.constrainInitialData(obj: T) = constrainInitialDataImpl(obj)

@Suppress("UNCHECKED_CAST")
private fun <T> getDataOrNullImpl(obj: Any?): T? =
    when (obj) {
        is CoWContainer<*, *> ->
            obj.data as T
        is CoWCoroutinesContainer<*, *> ->
            obj.data as T
        else ->
            null
    }
@Suppress("NOTHING_TO_INLINE", "UnusedReceiverParameter")
internal inline fun <T: Any> CoWContainer<T, *>.getDataOrNull(obj: Any?): T? = getDataOrNullImpl(obj)
@Suppress("NOTHING_TO_INLINE", "UnusedReceiverParameter")
internal inline fun <T: Any> CoWCoroutinesContainer<T, *>.getDataOrNull(obj: Any?): T? = getDataOrNullImpl(obj)

@Suppress("UNCHECKED_CAST")
private fun <T> getFrozenDataOrNullImpl(obj: Any?): T? =
    when (obj) {
        is CoWContainer<*, *> ->
            (obj.data as T).takeIf { obj.immutable }
        is CoWCoroutinesContainer<*, *> ->
            (obj.data as T).takeIf { obj.immutable }
        else ->
            null
    }
@Suppress("NOTHING_TO_INLINE", "UnusedReceiverParameter")
internal inline fun <T: Any> CoWContainer<T, *>.getFrozenDataOrNull(obj: Any?): T? =
    getFrozenDataOrNullImpl(obj)
@Suppress("NOTHING_TO_INLINE", "UnusedReceiverParameter")
internal inline fun <T: Any> CoWCoroutinesContainer<T, *>.getFrozenDataOrNull(obj: Any?): T? =
    getFrozenDataOrNullImpl(obj)