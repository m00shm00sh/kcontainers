package com.moshy.containers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class LexicographicalCompareTest {

    companion object {
        @JvmStatic
        fun cases() = listOf(
            arrayOf("eq", listOf(1, 2), listOf(1, 2), 0),
            arrayOf("lt[0]", listOf(1, 2), listOf(2, 1), -1),
            arrayOf("lt[1]", listOf(1, 2), listOf(1, 3), -1),
            arrayOf("lt[#]", listOf(1, 2), listOf(1), -1),
            arrayOf("gt[0]", listOf(1, 2), listOf(0, 2), 1),
            arrayOf("gt[1]", listOf(1, 2), listOf(1, 1), 1),
            arrayOf("gt[#]", listOf(1, 2), listOf(1, 2, 3), 1)
        ).map { Arguments.of(*it) }
    }

    @MethodSource("cases")
    @ParameterizedTest(name="{0}")
    fun `test cmp`(name: String, a: List<Int>, b: List<Int>, cmp: Int) {
       assertEquals(cmp, a.compareTo(b))
    }

}