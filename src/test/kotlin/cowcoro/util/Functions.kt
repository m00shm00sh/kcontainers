package com.moshy.containers.cowcoro.util

import com.moshy.containers.coroutines.CopyOnWriteContainer

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
