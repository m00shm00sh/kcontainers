package com.moshy.containers.util

import com.moshy.containers.CopyOnWriteContainer

import org.junit.jupiter.api.Assertions.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible


@Suppress("UNCHECKED_CAST")
internal fun <C: Any> CopyOnWriteContainer<C, *>.containerRef(): C {
    val getActive = this::class.memberProperties.single { it.name == "data" }
        .getter
        .also { it.isAccessible = true }
        as KProperty1.Getter<CopyOnWriteContainer<C, *>, C>
    return getActive.invoke(this)
}

// return type is Collection<E> instead of List<E> because Iterator<E> makes no guarantees about ordering or uniqueness
internal fun <E> Iterator<E>.toCollection() =
    buildList { while (hasNext()) add(next()) } as Collection<E>
