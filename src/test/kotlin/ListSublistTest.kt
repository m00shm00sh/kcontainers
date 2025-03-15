package com.moshy.containers

import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class ListSublistTest {


    @ParameterizedTest
    @MethodSource("argsForTo")
    fun `test ToOrNull`(toIndex: Int, expect: List<Int>?) {
        assertEquals(expect, input.subListToOrNull(toIndex))
    }

    @ParameterizedTest
    @MethodSource("argsForTo")
    fun `test To`(toIndex: Int, expect: List<Int>?) {
        when {
            expect == null -> {
                val t = assertThrows<IndexOutOfBoundsException> {
                    input.subListTo(toIndex)
                }
                assertNotNull(t.message)
                assertContains(t.message!!, "toIndex")
            }

            else -> assertEquals(expect, input.subListTo(toIndex))
        }
    }
    @ParameterizedTest
    @MethodSource("argsForFrom")
    fun `test FromOrNull`(fromIndex: Int, expect: List<Int>?) {
        assertEquals(expect, input.subListFromOrNull(fromIndex))
    }

    @ParameterizedTest
    @MethodSource("argsForFrom")
    fun `test From`(fromIndex: Int, expect: List<Int>?) {
        when {
            expect == null -> {
                val t = assertThrows<IndexOutOfBoundsException> {
                    input.subListFrom(fromIndex)
                }
                assertNotNull(t.message)
                assertContains(t.message!!, "fromIndex")
            }

            else -> assertEquals(expect, input.subListFrom(fromIndex))
        }
    }

    private companion object {
        val input = listOf(1, 2, 3)

        @JvmStatic
        fun argsForTo() =
            listOf(
                listOf(2, listOf(1, 2)),
                listOf(3, input),
                listOf(-1, null),
                listOf(4, null),
                listOf(0, emptyList<Int>())
            ).map {
                Arguments.of(*it.toTypedArray())
            }

        @JvmStatic
        fun argsForFrom() =
            listOf(
                listOf(0, input),
                listOf(1, listOf(2, 3)),
                listOf(-1, null),
                listOf(4, null),
                listOf(3, emptyList<Int>())
            ).map {
                Arguments.of(*it.toTypedArray())
            }
    }

}