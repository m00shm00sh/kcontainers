package com.moshy.containers.cowcoro

import com.moshy.containers.coroutines.CopyOnWriteHashSet
import com.moshy.containers.coroutines.CopyOnWriteSet
import com.moshy.containers.util.toCollection
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import kotlin.test.assertContentEquals

/* Test that we forward Set behavior. CoW behavior already tested in CopyOnWriteContainerTest. */
class CopyOnWriteSetTest {
    private lateinit var c0: CopyOnWriteSet<String>
    private val s = "a"

    @Test
    fun `test MutableSet`() = runTest {
        val r1 = c0.write { add(s) }
        val r2 = c0.write { add(s) }
        assertAll(
            { Assertions.assertTrue(r1) },
            { Assertions.assertFalse(r2) }
        )
    }

    @Test
    fun `test iterator snapshot`() = runTest {
        var iter = c0.iterator()
        c0.write { add(s) }
        assertAll(
            { assertContentEquals(emptySet(), iter.toCollection()) }
        )
        iter = c0.iterator()
        assertAll(
            { assertContentEquals(setOf(s), iter.toCollection()) }
        )
    }

    @Test
    fun set() {
        c0 = CopyOnWriteHashSet(setOf(s))
        val inC0 = s in c0
        val inC0_2 = c0.containsAll(setOf(s))
        assertAll(
            { Assertions.assertTrue(inC0) },
            { Assertions.assertTrue(inC0_2) }
        )
    }

    @BeforeEach
    fun initContainer() {
        c0 = CopyOnWriteHashSet()
    }
}