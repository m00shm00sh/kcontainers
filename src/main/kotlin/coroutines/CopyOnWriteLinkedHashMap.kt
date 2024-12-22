package com.moshy.containers.coroutines

/**
 * Copy on write [LinkedHashMap].
 * @see CopyOnWriteMap
 */
class CopyOnWriteLinkedHashMap<K, V>(initialData: Map<K, V> = emptyMap())
    : CopyOnWriteMap<K, V>(initialData, ::LinkedHashMap)