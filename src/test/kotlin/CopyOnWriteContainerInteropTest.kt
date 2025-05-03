package com.moshy.containers

import com.moshy.containers.cowcoro.util.containerRef
import com.moshy.containers.cowcoro.util.CoWTracer as CoroCoWTracer
import com.moshy.containers.util.CoWTracer as LockingCoWTracer
import com.moshy.containers.util.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import kotlin.concurrent.thread

/*
 * Tests copy-on-write behavior interoperability between the locking and coroutine variants.
 */

class CopyOnWriteContainerInteropTest {
    private lateinit var coroC: CoroCoWTracer
    private lateinit var lockingC: LockingCoWTracer

    @Test
    fun `verify frozen coroC is usable by lockingC`() = runTest {
        coroC.freeze()
        lockingC = LockingCoWTracer(coroC)
        assertSame(coroC.data, lockingC.data)
    }

    @Test
    fun `verify frozen lockingC is usable by coroC`() {
        lockingC.freeze()
        coroC = CoroCoWTracer(lockingC)
        assertSame(lockingC.data, coroC.data)
    }

    @Test
    fun `verify equals compares underlying container`() {
        assertEquals(coroC, lockingC)
    }

    @Test
    fun `verify downstream propagation`() = runTest {
        val c1 = CoroCoWTracer(coroC)
        coroC.write { clear() }
        // we have double wrapping because c0 was never frozen
        val c1Ref = (c1.containerRef() as CoroCoWTracer).containerRef() as TracingMutableCollectionOfObjects
        assertEquals(1, c1Ref.modCount)
    }

    @BeforeEach
    fun initContainer() {
        coroC = CoroCoWTracer()
        lockingC = LockingCoWTracer()
    }
}
