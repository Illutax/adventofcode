package day03

import org.junit.jupiter.api.Test
import readInput
import kotlin.test.assertEquals

class Day03Test {

    private val path = "03_test"

    @Test
    fun `parse input`() {
        val input = readInput(path, ::mapInput);
        val expected: Input = listOf(
            listOf(0, 0, 1, 0, 0),
            listOf(1, 1, 1, 1, 0),
            listOf(1, 0, 1, 1, 0),
            listOf(1, 0, 1, 1, 1),
            listOf(1, 0, 1, 0, 1),
            listOf(0, 1, 1, 1, 1),
            listOf(0, 0, 1, 1, 1),
            listOf(1, 1, 1, 0, 0),
            listOf(1, 0, 0, 0, 0),
            listOf(1, 1, 0, 0, 1),
            listOf(0, 0, 0, 1, 0),
            listOf(0, 1, 0, 1, 0)
        )
        assertEquals(expected, input)
    }

    @Test
    fun `part 1 example`()
    {
        val input = readInput(path, ::mapInput);
        assertEquals(198, part1(input))
    }

    @Test
    fun `part 2 example`()
    {
        val input = readInput(path, ::mapInput);
        assertEquals(230, part2(input))
    }
}