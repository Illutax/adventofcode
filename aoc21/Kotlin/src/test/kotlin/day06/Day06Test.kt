package day06

import org.junit.jupiter.api.Test
import readInput
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
        assertEquals(listOf(2, 3, 2, 0, 1), tick(listOf(3, 4, 3, 1, 2)))
    }

    @Test
    fun `tick 18 times`() {
        val input = readInput(path, ::mapInput);
        assertEquals(26, tickNTimes(input, 18).count())
    }

    @Test
    fun `amount of fish after 18 nticks`() {
        val input = readInput(path, ::mapInput);
        assertEquals(26, tickNTimes(input, 18).count())
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