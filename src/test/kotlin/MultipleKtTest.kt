package com.moshy.containers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class MultipleTest {

    // use a LHS so we have predictable iteration order without invoking the List<T>.multiple() extension overload
    private val collection = LinkedHashSet<Int>().apply {
        addAll(listOf(1, 2, 3, 4, 5))
    }
    private val isEven = { it: Int -> it % 2 == 0 }
    private val isOdd = { it: Int -> it % 2 != 0 }
    private val collectionExpectedOdd = 3
    private val collectionExpectedEven = 2
    private val collectionExpectedAll = 5

    @Test
    fun `test multiple num range`() {
        val t = assertThrows<IllegalArgumentException> {
            collection.multiple(-1)
        }
        assertEquals("num is negative", t.message)
    }
    @Test
    fun `test multiple on collection with default predicate`() {
        assertAll(
            { assertEquals(listOf(1, 2, 3, 4, 5), collection.multiple(collectionExpectedAll)) },
            { assertEquals(listOf(2, 4), collection.filter(isEven).multiple(collectionExpectedEven)) }
        )
    }

    @Test
    fun `test multiple on collection with default predicate matching too many elements`() {
        val t = assertThrows<IllegalArgumentException> {
            collection.multiple(collectionExpectedAll - 1)
        }
        assertEquals("Collection has too many elements", t.message)
    }

    @Test
    fun `test multiple on collection with default predicate matching too few elements`() {
        val t = assertThrows<NoSuchElementException> {
            collection.multiple(collectionExpectedAll + 1)
        }
        assertEquals("Collection has missing elements", t.message)
    }
    @Test
    fun `test multiple on collection with custom predicate`() {
        assertAll(
            { assertEquals(listOf(1, 3, 5), collection.multiple(collectionExpectedOdd, isOdd)) },
        )
    }

    @Test
    fun `test multiple on collection with custom predicate matching too many elements`() {
        val t = assertThrows<IllegalArgumentException> {
            collection.multiple(collectionExpectedEven, isOdd)
        }
        assertEquals("Collection has too many elements", t.message)
    }

    @Test
    fun `test multiple on collection with custom predicate matching too few elements`() {
        val t = assertThrows<NoSuchElementException> {
            collection.multiple(collectionExpectedOdd, isEven)
        }
        assertEquals("Collection has missing elements", t.message)
    }

    @Test
    fun `test multiple list num range`() {
       val t = assertThrows<IllegalArgumentException> {
           listOf<Unit>().multiple(-1)
       }
        assertEquals("num is negative", t.message)
    }

    @Test
    fun `test multiple list pass-through`() {
        val list = listOf(1, 2, 3)
        assertTrue(list === list.multiple(3))
    }

    @Test
    fun `test multiple(0)`() {
        assertDoesNotThrow {
            emptyList<Unit>().multiple(0)
        }
    }

    @Test
    fun `test multipleOrNull num range`() {
        val t = assertThrows<IllegalArgumentException> {
            collection.multipleOrNull(-1)
        }
        assertEquals("num is negative", t.message)
    }

    @Test
    fun `test multipleOrNull on collection with default predicate`() {
        assertAll(
            { assertEquals(listOf(1, 2, 3, 4, 5), collection.multipleOrNull(5)) },
            { assertEquals(listOf(2, 4), collection.filter(isEven).multipleOrNull(collectionExpectedEven)) }
        )
    }

    @Test
    fun `test multipleOrNull on collection with default predicate matching too many elements`() {
        assertNull(collection.multipleOrNull(collectionExpectedAll - 1))
    }

    @Test
    fun `test multipleOrNull on collection with default predicate matching too few elements`() {
        assertNull(collection.multipleOrNull(collectionExpectedAll + 1))
    }
    @Test
    fun `test multipleOrNull on collection with custom predicate`() {
        assertAll(
            { assertEquals(listOf(1, 3, 5), collection.multipleOrNull(collectionExpectedOdd, isOdd)) },
        )
    }

    @Test
    fun `test multipleOrNull on collection with custom predicate matching too many elements`() {
        assertNull(collection.multipleOrNull(collectionExpectedEven, isOdd))
    }

    @Test
    fun `test multipleOrNull on collection with custom predicate matching too few elements`() {
        assertNull(collection.multipleOrNull(collectionExpectedOdd, isEven))
    }

    @Test
    fun `test multipleOrNull list num range`() {
        val t = assertThrows<IllegalArgumentException> {
            listOf<Unit>().multipleOrNull(-1)
        }
        assertEquals("num is negative", t.message)
    }

    @Test
    fun `test multipleOrNull list pass-through`() {
        val list = listOf(1, 2, 3)
        assertTrue(list === list.multipleOrNull(3))
    }

    @Test
    fun `test multipleOrNull(0)`() {
        val v = assertDoesNotThrow {
            emptyList<Unit>().multipleOrNull(0)
        }
        assertNotNull(v)
    }

}