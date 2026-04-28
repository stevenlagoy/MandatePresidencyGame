package com.stevenlagoy.presidency.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import kotlin.random.Random

class RandomUtilsTest {

    @Test
    fun `WHEN chance less than 0 THEN always returns false`() {
        val r = Random(123)
        repeat(100) {
            assertFalse(r.chance(-0.5))
        }
    }

    @Test
    fun `WHEN chance greater than 1 THEN always returns true`() {
        val r = Random(123)
        repeat(100) {
            assertTrue(r.chance(1.5))
        }
    }

    @RepeatedTest(5)
    fun `WHEN chance equals 0_5 THEN distribution is approximately correct`() {
        val r = Random(123)
        val trials = 100_000
        val successes = (1..trials).count { r.chance(0.5) }
        val ratio = successes.toDouble() / trials

        assertTrue(ratio in 0.45..0.55, "ratio=$ratio")
    }

    @Test
    fun `WHEN rollDie called THEN result is within bounds`() {
        val r = Random(123)
        repeat(10_000) {
            val value = r.rollDie(6)
            assertTrue(value in 1..5) // nextInt(1, sides) => [1, sides)
        }
    }

    @Test
    fun `WHEN rollDice with dice and sides THEN size and bounds are correct`() {
        val r = Random(123)
        val result = r.rollDice(dice = 5, sides = 6)

        assertEquals(5, result.size)
        result.forEach {
            assertTrue(it in 0..5) // nextInt(sides)
        }
    }

    @Test
    fun `WHEN rollDice with varying sides THEN each value respects its bound`() {
        val r = Random(123)
        val sides = intArrayOf(2, 4, 6, 8)

        val result = r.rollDice(sides)

        assertEquals(sides.size, result.size)
        result.forEachIndexed { i, v ->
            assertTrue(v in 0 until sides[i])
        }
    }

    @Test
    fun `WHEN nextFloat with range THEN value is within range`() {
        val r = Random(123)
        repeat(10_000) {
            val v = r.nextFloat(5.0f, 10.0f)
            assertTrue(v >= 5.0f && v < 10.0f)
        }
    }

    @Test
    fun `WHEN select from empty collection THEN returns null`() {
        val r = Random(123)
        assertNull(r.select(emptyList<Int>()))
    }

    @Test
    fun `WHEN select from collection THEN result is element of collection`() {
        val r = Random(123)
        val items = listOf(1, 2, 3, 4)

        repeat(1000) {
            val v = r.select(items)
            assertTrue(v in items)
        }
    }

    @Test
    fun `WHEN weightedSelect empty THEN returns null`() {
        val r = Random(123)
        assertNull(r.weightedSelect(emptyMap<Int, Int>()))
    }

    @Test
    fun `WHEN weightedSelect single item THEN always returns that item`() {
        val r = Random(123)
        val map = mapOf("A" to 10)

        repeat(100) {
            assertEquals("A", r.weightedSelect(map))
        }
    }

    @RepeatedTest(5)
    fun `WHEN weightedSelect with skewed weights THEN distribution favors larger weight`() {
        val r = Random(123)
        val items = mapOf("A" to 1, "B" to 9)

        val trials = 100_000
        val counts = mutableMapOf("A" to 0, "B" to 0)

        repeat(trials) {
            val v = r.weightedSelect(items)
            counts[v!!] = counts.getValue(v) + 1
        }

        val ratioB = counts.getValue("B").toDouble() / trials
        assertTrue(ratioB > 0.8, "ratioB=$ratioB")
    }

    @Test
    fun `WHEN probability less than 0 THEN returns 0`() {
        val r = Random(123)
        assertEquals(0, r.probabilisticCount(-0.1))
    }

    @Test
    fun `WHEN probability greater than 1 THEN returns Int_MAX_VALUE`() {
        val r = Random(123)
        assertEquals(Int.MAX_VALUE, r.probabilisticCount(1.1))
    }

    @RepeatedTest(5)
    fun `WHEN probability 0_5 THEN average count is approximately expected`() {
        val r = Random(123)
        val trials = 50_000

        val avg = (1..trials)
            .map { r.probabilisticCount(0.5) }
            .average()

        // Geometric distribution mean = p / (1 - p) = 1 for p=0.5
        assertTrue(avg in 0.8..1.2, "avg=$avg")
    }

    @Test
    fun `WHEN chance wrapper called THEN delegates correctly`() {
        val r1 = Random(123)
        val r2 = Random(123)
        repeat(1000) {
            assertEquals(r1.chance(0.25), chance(0.25, r2))
        }
    }

    @Test
    fun `WHEN rollDie wrapper called THEN delegates correctly`() {
        val r1 = Random(123)
        val r2 = Random(123)
        repeat(1000) {
            assertEquals(r1.rollDie(6), rollDie(6, r2))
        }
    }

    @Test
    fun `WHEN rollDice wrapper called THEN delegates correctly`() {
        val r1 = Random(123)
        val r2 = Random(123)
        repeat(100) {
            assertEquals(r1.rollDice(5, 6), rollDice(5, 6, r2))
        }
    }

    @Test
    fun `WHEN rollDice array wrapper called THEN delegates correctly`() {
        val r1 = Random(123)
        val r2 = Random(123)
        val sides = intArrayOf(2, 3, 4)
        repeat(100) {
            assertEquals(r1.rollDice(sides), rollDice(sides, r2))
        }
    }

    @Test
    fun `WHEN nextFloat wrapper called THEN delegates correctly`() {
        val r1 = Random(123)
        val r2 = Random(123)
        repeat(1000) {
            assertEquals(r1.nextFloat(2f, 5f), nextFloat(2f, 5f, r2))
        }
    }

    @Test
    fun `WHEN nextPercent wrapper called THEN delegates correctly`() {
        val r1 = Random(123)
        val r2 = Random(123)
        repeat(1000) {
            assertEquals(r1.nextPercent(), nextPercent(r2))
        }
    }

    @Test
    fun `WHEN probabilisticCount wrapper called THEN delegates correctly`() {
        val r1 = Random(123)
        val r2 = Random(123)
        repeat(1000) {
            assertEquals(r1.probabilisticCount(0.3), probabilisticCount(0.3, r2))
        }
    }

    @Test
    fun `WHEN select vararg empty THEN returns null`() {
        val r = Random(123)
        assertNull(r.select(*emptyArray<Int>()))
    }

    @Test
    fun `WHEN select vararg THEN result is from inputs`() {
        val r = Random(123)
        val values = arrayOf(10, 20, 30)

        repeat(1000) {
            val v = r.select(*values)
            assertTrue(v in values)
        }
    }

    @Test
    fun `WHEN select collection wrapper THEN delegates correctly`() {
        val r1 = Random(123)
        val r2 = Random(123)
        val list = listOf(1, 2, 3)

        repeat(1000) {
            assertEquals(r1.select(list), select(list, r2))
        }
    }

    @Test
    fun `WHEN select array wrapper THEN delegates correctly`() {
        val r1 = Random(123)
        val r2 = Random(123)
        val arr = arrayOf("A", "B", "C")

        repeat(1000) {
            assertEquals(r1.select(arr.toList()), select(arr, r2))
        }
    }

    @Test
    fun `WHEN select collection distribution THEN roughly uniform`() {
        val r = Random(123)
        val items = listOf(1, 2, 3, 4)
        val counts = IntArray(4)

        val trials = 100_000
        repeat(trials) {
            val v = r.select(items)!!
            counts[v - 1]++
        }

        val expected = trials / 4.0
        counts.forEach {
            val ratio = it / expected
            assertTrue(ratio in 0.9..1.1, "ratio=$ratio")
        }
    }

    @Test
    fun `WHEN weightedSelect list wrapper THEN delegates correctly`() {
        val r1 = Random(123)
        val r2 = Random(123)
        val items = listOf("A", "B")
        val weights = listOf(1, 2)

        repeat(1000) {
            assertEquals(
                r1.weightedSelect(items, weights),
                weightedSelect(items, weights, r2)
            )
        }
    }

    @Test
    fun `WHEN weightedSelect map wrapper THEN delegates correctly`() {
        val r1 = Random(123)
        val r2 = Random(123)
        val map = mapOf("A" to 1, "B" to 2)

        repeat(1000) {
            assertEquals(r1.weightedSelect(map), weightedSelect(map, r2))
        }
    }

    @Test
    fun `WHEN weightedSelect with zero weights THEN ignores them`() {
        val r = Random(123)
        val map = mapOf("A" to 0, "B" to 10)

        repeat(1000) {
            assertEquals("B", r.weightedSelect(map))
        }
    }

    @Test
    fun `WHEN weightedSelect with negative weights THEN treated as zero`() {
        val r = Random(123)
        val map = mapOf("A" to -5, "B" to 5)

        repeat(1000) {
            assertEquals("B", r.weightedSelect(map))
        }
    }

    @RepeatedTest(5)
    fun `WHEN weightedSelect list distribution THEN matches weights`() {
        val r = Random(123)
        val items = listOf("A", "B", "C")
        val weights = listOf(1, 2, 7)

        val trials = 100_000
        val counts = mutableMapOf("A" to 0, "B" to 0, "C" to 0)

        repeat(trials) {
            val v = r.weightedSelect(items, weights)!!
            counts[v] = counts.getValue(v) + 1
        }

        val ratioC = counts.getValue("C").toDouble() / trials
        assertTrue(ratioC in 0.65..0.75, "ratioC=$ratioC")
    }

    @Test
    fun `WHEN probability zero THEN always zero`() {
        val r = Random(123)
        repeat(1000) {
            assertEquals(0, r.probabilisticCount(0.0))
        }
    }

    @RepeatedTest(5)
    fun `WHEN probability high THEN counts are frequently large`() {
        val r = Random(123)
        val trials = 50_000

        val avg = (1..trials)
            .map { r.probabilisticCount(0.9) }
            .average()

        // Expected mean = p / (1 - p) = 9
        assertTrue(avg in 7.0..11.0, "avg=$avg")
    }

    @Test
    fun `WHEN nextInt wrapper called THEN delegates correctly`() {
        val r1 = Random(123)
        val r2 = Random(123)
        repeat(1000) {
            assertEquals(r1.nextInt(5, 10), nextInt(5, 10, r2))
        }
    }

    @Test
    fun `WHEN nextInt bounds THEN values are in range`() {
        val r = Random(123)
        repeat(10_000) {
            val v = nextInt(5, 10, r)
            assertTrue(v in 5 until 10)
        }
    }
}
