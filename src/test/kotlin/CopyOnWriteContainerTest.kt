package com.moshy.containers

import com.moshy.containers.util.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import kotlin.concurrent.thread

/*
 * Tests copy-on-write behavior, basic container operation forwarding (equals, hashCode, toString), helper functions.
 */

class CopyOnWriteContainerTest {
    private lateinit var c0: CoWTracer

    @Test
    fun `test mutation works on copy`() {
        val c0Ref0 = c0.containerRef()
        c0.write { }
        val c0Ref1 = c0.containerRef()
        assertNotSame(c0Ref0, c0Ref1)
    }

    @Test
    fun `test snapshot iterator`() {
        val iter1 = c0.iterator()
        c0.write { clear() }
        val iter2 = c0.iterator()
        assertAll(
            { assertFalse(iter1.hasNext()) },
            { assertTrue(iter2.hasNext()) }
        )
    }
    @Test
    fun `verify writes are serialized`() {
        // Hacky because JVM-dependent, but CoWContainer uses locks instead of coroutine mutexes so use threads
        Assumptions.assumeTrue(Runtime.getRuntime().availableProcessors() > 1,
            "multiple cores are necessary for this test"
        )
        val sleepTimeMillis = 100L // tunable
        val preTime = System.currentTimeMillis()
        val threads = (1..2).map { thread {
            c0.write {
                clear()
                Thread.sleep(sleepTimeMillis, 0)
            }
        } }
        for (t in threads)
            t.join()
        val postTime = System.currentTimeMillis()
        assertTrue((postTime - preTime) >= 2 * sleepTimeMillis)
    }

    @Test
    fun `verify writeOnce freezes the container`() {
        c0.writeOnce { }
        assertThrows<UnsupportedOperationException> {
            c0.write { }
        }
    }

    @Test
    fun `verify equals compares underlying container`() {
        val c1 = CoWTracer()
        assertEquals(c0, c1)
    }

    @Test
    fun `verify frozen container gets propagated`() {
        c0.freeze()
        val c0Ref = c0.containerRef()
        val c1 = CoWTracer(c0)
        val c1Ref = c1.containerRef()
        assertSame(c0Ref, c1Ref)
    }

    @Test
    fun `verify hashCode`() {
        val c0Ref = c0.containerRef() as TracingMutableCollectionOfObjects
        assertEquals(c0Ref.hashCode(), c0.hashCode())
    }

    @Test
    fun `verify toString`() {
        assertEquals(TracingMutableCollectionOfObjects.STR, c0.toString())
    }

    @Test
    fun `verify require container`() {
        open class C
        class C2(unused: C = C()): C()
        assertThrows<IllegalArgumentException> {
            object : CopyOnWriteContainer<C, C2>(C(), ::C2) { }
        }
    }

    @Test
    fun `verify downstream propagation`() {
        val c1 = CoWTracer(c0)
        c0.write { clear() }
        // we have double wrapping because c0 was never frozen
        val c1Ref = (c1.containerRef() as CoWTracer).containerRef() as TracingMutableCollectionOfObjects
        assertEquals(1, c1Ref.modCount)
    }

    @BeforeEach
    fun initContainer() {
        c0 = CoWTracer()
    }
}
