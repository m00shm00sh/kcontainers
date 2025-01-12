package com.moshy.containers

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class ListTransposeTest {

    private val input = listOf(
        listOf(1, 2, 3),
        listOf(4, 5, 6),
        listOf(7, 8, 9)
    )
    private val expected1 = listOf(
        listOf(1, 4, 7),
        listOf(2, 5, 8),
        listOf(3, 6, 9)
    )
    private val expected2 = listOf(12, 15, 18)

    @Test
    fun `test basic`() {
        assertEquals(expected1, input.transpose())
    }

    @Test
    fun `test transform`() {
        assertEquals(expected2, input.transpose { it.sum() })
    }

}