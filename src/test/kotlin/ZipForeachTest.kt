package com.moshy.containers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ZipForeachTest {

    private val a = listOf(1, 2)
    private val b = listOf(3, 4)

    @Test
    fun `test basic`() {
        var c = 0
        a.zipForEach(b) { a, b ->
            c += a + b
        }
        assertEquals(a.sum() + b.sum(), c)
    }

    @Test
    fun `test type`() {
        val c = a.zipForEach(b) { a, b -> }
        assertEquals(Unit, c)
    }

}