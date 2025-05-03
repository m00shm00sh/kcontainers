package com.moshy.containers

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertNull


class SortedSetTest {

    /* constructions */

    @Test
    fun `test assertIsSortedSet`() {
        assertDoesNotThrow {
            listOf(1, 2, 3).assertIsSortedSet()
        }
    }

    @Test
    fun `test copyToSortedSet 1`() {
        val s = listOf(1, 2, 3, 2, 1).copyToSortedSet()
        assertContentEquals(listOf(1, 2, 3), s)
    }

    @Test
    fun `test copyToSortedSet 2`() {
        val s = listOf(0, 1, 2, 3, 0, 1).copyToSortedSet {
            a, b -> b.compareTo(a)
        }
        assertContentEquals(listOf(3, 2, 1, 0), s)
    }

    /* sortedset properties */

    @Test
    fun `test set intersection`() {
        val s = listOf(1, 2, 3, 4).assertIsSortedSet()
        val s1 = listOf(1, 2).assertIsSortedSet()
        val s2 = listOf(1, 4).assertIsSortedSet()
        val s3 = listOf(2, 5).assertIsSortedSet()
        assertAll(
            { assertContentEquals(s1 as Iterable<Int>, s.intersect(s1)) },
            { assertContentEquals(s2 as Iterable<Int>, s.intersect(s2)) },
            { assertContentEquals(listOf(2), s.intersect(s3)) }
        )
    }

    @Test
    fun `test comparator`() {
        val neg = Comparator<Int> { a, b -> b.compareTo(a) }
        val l = listOf(3, 2, 1).assertIsSortedSet(neg)
        val ll = listOf(3, 2).assertIsSortedSet(neg)
        assertContentEquals(ll as Iterable<*>, l.intersect(ll))
    }

    @Test
    fun `test headSet`() {
        val s = listOf(1, 2, 3).assertIsSortedSet()
        assertAll(
            { assertContentEquals(listOf(1, 2), s.headSet(3)) },
            { assertContentEquals(listOf(1, 2, 3), s.headSet(4)) },
            { assertContentEquals(emptyList(), s.headSet(0)) }
        )
    }

    @Test
    fun `test headSet chain`() {
        val s = listOf(1, 2, 3).assertIsSortedSet()
        val h = s.headSet(3)
        assertAll(
            { assertContentEquals(listOf(1), h.headSet(2)) },
            { assertThrows<IllegalArgumentException>{
                h.headSet(4)
            } }
        )
    }
    @Test
    fun `test tailSet`() {
        val s = listOf(1, 2, 3).assertIsSortedSet()
        assertAll(
            { assertContentEquals(listOf(2, 3), s.tailSet(2)) },
            { assertContentEquals(emptyList(), s.tailSet(4)) },
            { assertContentEquals(listOf(1, 2, 3), s.tailSet(0)) }
        )
    }

    @Test
    fun `test tailSet chain`() {
        val s = listOf(1, 2, 3).assertIsSortedSet()
        val h = s.tailSet(2)
        assertAll(
            { assertContentEquals(listOf(3), h.tailSet(3)) },
            { assertThrows<IllegalArgumentException>{
                h.tailSet(1)
            } }
        )
    }
    @Test
    fun `test subSet`() {
        val s = listOf(1, 2, 3, 4, 5).assertIsSortedSet()
        assertAll(
            { assertContentEquals(listOf(2, 3, 4), s.subSet(2, 5)) },
            { assertContentEquals(listOf(1, 2, 3, 4, 5), s.subSet(1, 6)) },
            { assertContentEquals(emptyList(), s.subSet(2, 2)) },
            { assertThrows<IllegalArgumentException> {
                s.subSet(4, 3)
            }}
        )
    }

    @Test
    fun `test subSet chain`() {
        val s = listOf(1, 2, 3, 4, 5).assertIsSortedSet()
        val h = s.subSet(2, 5)
        assertAll(
            { assertContentEquals(listOf(3), h.subSet(3, 4)) },
            { assertThrows<IllegalArgumentException>{
                h.subSet(0, 4)
            } },
            { assertThrows<IllegalArgumentException>{
                h.subSet(3, 6)
            } }
        )
    }

    @Test
    fun `test composition of restrictions`() {
        val s = listOf(1, 2, 3, 4, 5).assertIsSortedSet()
        val sHead = s.headSet(4)
        val sHeadTail = sHead.tailSet(2)
        val sSubset = s.subSet(2, 4)
        assertContentEquals(sSubset as Iterable<*>,sHeadTail)
    }

    @Test
    fun `test first and last`() {
        val s = listOf(1, 2).assertIsSortedSet()
        val e = emptyList<Int>().assertIsSortedSet()
        assertAll(
            { assertEquals(1, s.first()) },
            { assertEquals(2, s.last()) },
            { assertNull(e.firstOrNull()) },
            { assertNull(e.lastOrNull()) }
        )
    }

    /* set properties */

    @Test
    fun `test live size`() {
        val l = mutableListOf(1, 2, 3)
        val s = l.assertIsSortedSet()
        l.add(4)
        assertEquals(4, s.size)
    }

    @Test
    fun `test set contains`() {
        val s = listOf(1, 2, 3).assertIsSortedSet()
        assertAll(
            { assertTrue(2 in s) },
            { assertFalse(4 in s) }
        )
    }

    @Test
    fun `test set containsAll`() {
        val s = listOf(1, 2, 3).assertIsSortedSet()
        assertAll(
            { assertTrue(s.containsAll(listOf(1, 2))) },
            { assertFalse(s.containsAll(listOf(1, 4))) },
            { assertTrue(s.containsAll(listOf(1, 2).assertIsSortedSet())) },
            { assertFalse(s.containsAll(listOf(1, 4).assertIsSortedSet())) },
        )
    }

    @Test
    fun `test set iterator`() {
        val s = listOf(1, 2, 3).assertIsSortedSet()
        val l = listOf(1, 2, 3)
        assertContentEquals(l, s)
    }

    @Test
    fun `test set equals`() {
        val s = listOf(1, 2, 3).assertIsSortedSet()
        val l = listOf(1, 2, 3)
        val f1 = listOf(2, 1, 1)
        assertAll(
            { assertTrue(s == l) },
            { assertTrue(s == l.assertIsSortedSet()) },
            { assertFalse(s == f1) }
        )
    }

    @Test
    fun `test set size`() {
        val s = listOf(1, 2, 3).assertIsSortedSet()
        assertEquals(3, s.size)
    }

    @Test
    fun `test isEmpty`() {
        assertAll(
            { assertTrue(emptyList<Int>().assertIsSortedSet().isEmpty()) },
            { assertFalse(listOf(1).assertIsSortedSet().isEmpty()) },
        )
    }

    @Test
    fun `test hashCode`() {
        val l = listOf(1, 2, 3)
        assertEquals(l.hashCode(), l.assertIsSortedSet().hashCode())
    }

    /* test buildcopy */

    @Test
    fun `test buildCopy`() {
        val s1 = emptyList<Int>().assertIsSortedSet()
        val s2 = s1.buildCopy {  }
        assertNotSame(s2, s1)
    }

    @Test
    fun `test buildCopy add`() {
        val s1 = listOf(1, 2, 3).assertIsSortedSet()
        val s2 = s1.buildCopy {
            assertTrue {
                add(4)
            }
            assertFalse {
                add(3)
            }
        }
        assertContentEquals(listOf(1, 2, 3, 4), s2)
    }

    @Test
    fun `test buildCopy add restricted range`() {
        val s1 = listOf(1, 3, 5).assertIsSortedSet().subSet(2, 4)
        val s2 = s1.buildCopy {
            assertThrows<IllegalArgumentException>("low") {
                add(0)
            }
            assertThrows<IllegalArgumentException>("high") {
                add(4)
            }
            assertTrue {
                add(2)
            }
            assertFalse {
                add(3)
            }
        }
        assertContentEquals(listOf(2, 3), s2)
    }

    @Test
    fun `test buildCopy addAll`() {
        val s1 = listOf(1, 2, 3).assertIsSortedSet()
        val s2 = s1.buildCopy {
            assertFalse(addAll(listOf(2, 3)))
            assertTrue(addAll(listOf(3, 4)))
            assertTrue(addAll(listOf(5, 0)))
        }
        assertContentEquals(listOf(0, 1, 2, 3, 4, 5), s2)
    }

    @Test
    fun `test buildCopy remove`() {
        val s1 = listOf(1, 2, 3).assertIsSortedSet()
        val s2 = s1.buildCopy {
            assertTrue {
                remove(2)
            }
            assertFalse {
                remove(4)
            }
        }
        assertContentEquals(listOf(1, 3), s2)
    }

    @Test
    fun `test buildCopy removeAll`() {
        val s1 = listOf(1, 2, 3, 4).assertIsSortedSet()
        val s2 = s1.buildCopy {
            assertFalse(removeAll(listOf(5, 6)))
            assertTrue(removeAll(listOf(0, 1)))
            assertTrue(removeAll(listOf(2, 3)))
        }
        assertContentEquals(listOf(4), s2)
    }

    @Test
    fun `test buildCopy retainAll`() {
        val s1 = listOf(1, 2, 3, 4).assertIsSortedSet()
        val s2 = s1.buildCopy {
            assertFalse(retainAll(listOf(4, 3, 2, 1, 2, 3)))
            assertTrue(retainAll(listOf(4, 3, 5, 4)))
            assertFalse(retainAll(listOf(4, 3, 3, 4)))
        }
        assertContentEquals(listOf(3, 4), s2)
    }

    @Test
    fun `test buildCopy clear`() {
        val s1 = listOf(1, 2, 3).assertIsSortedSet()
        val s2 = s1.buildCopy {
            assertDoesNotThrow {
                clear()
            }
        }
        assertTrue(s2.isEmpty())
    }

}