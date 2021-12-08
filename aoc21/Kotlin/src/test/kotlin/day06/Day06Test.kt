package day06

import org.junit.jupiter.api.Test
import util.readInput
import kotlin.test.assertEquals

class Day06Test {

    private val path = "06_test"

    @Test
    fun `parse input`() {
        val input = readInput(path, ::mapInput);
        assertEquals(listOf(3, 4, 3, 1, 2), input)
    }

    @Test
    fun `tick once`() {
        assertEquals(mapOf(Pair(0,1L), Pair(1,1), Pair(2,2), Pair(3,1)), tick(mapOf(Pair(1,1), Pair(2,1), Pair(3,2), Pair(4,1))))
    }

    @Test
    fun `tick once with collision`() {
        assertEquals(mapOf(Pair(6,3L), Pair(8,1)), tick(mapOf(Pair(0,1), Pair(7,2))))
    }

    @Test
    fun `tick 18 times`() {
        val input = readInput(path, ::mapInput);
        assertEquals(26, tickNTimes(input, 18))
    }

    @Test
    fun `amount of fish after 18 nticks`() {
        val input = readInput(path, ::mapInput);
        assertEquals(26, tickNTimes(input, 18))
    }

    @Test
    fun `part 1 example`() {
        val input = readInput(path, ::mapInput);
        assertEquals(5934, part1(input))
    }

    @Test
    fun `part 2 example`() {
        val input = readInput(path, ::mapInput);
        assertEquals(26_984_457_539, part2(input))
    }
}