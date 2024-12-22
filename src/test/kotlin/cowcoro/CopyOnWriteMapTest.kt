package com.moshy.containers.cowcoro

import com.moshy.containers.coroutines.CopyOnWriteLinkedHashMap
import com.moshy.containers.coroutines.CopyOnWriteMap
import com.moshy.containers.util.toCollection
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import kotlin.test.assertContentEquals

/* Test that we forward Map behavior. CoW behavior already tested in CopyOnWriteContainerTest.
*  LHM is used because entries.iterator().last() will have predictable behavior.
* */
class CopyOnWriteMapTest {
    private lateinit var c0: CopyOnWriteMap<String, Int>
    private val s = "a"

    @Test
    fun `test MutableMap`() = runTest {
        val r1 = c0.write { put(s, 1) }
        val r2 = c0.write { put(s, 2) }
        assertAll(
            { Assertions.assertNull(r1) },
            { Assertions.assertNotNull(r2) },
            { Assertions.assertEquals(1, c0.size) },
            { Assertions.assertEquals(2, c0[s]) }
        )
    }

    @Test
    fun `test iterator snapshot`() = runTest {
        // Map gives us an Iterable through .entries or alternately through .keys and .values
        val iter = c0.iterator()
        c0.write { put(s, 1) }
        assertAll(
            { assertContentEquals(emptySet(), iter.toCollection()) }
        )
        val kIter = c0.keys
        val vIter = c0.values
        c0.write { put(s + s, 2) }
        assertAll(
            { assertContentEquals(setOf(s) as Iterable<*>, kIter) },
            { assertContentEquals(setOf(1) as Iterable<*>, vIter) }
        )
    }

    @Test
    fun map() {
        c0 = CopyOnWriteLinkedHashMap(mapOf(s to 1))
        val inC0k = s in c0
        val inC0v = c0.containsValue(1)
        assertAll(
            { Assertions.assertTrue(inC0k) },
            { Assertions.assertTrue(inC0v) },
            { Assertions.assertFalse(c0.isEmpty()) }
        )
    }

    @BeforeEach
    fun initContainer() {
        c0 = CopyOnWriteLinkedHashMap()
    }
}