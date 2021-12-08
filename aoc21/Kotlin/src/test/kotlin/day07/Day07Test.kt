package day07

import org.junit.jupiter.api.Test
import util.readInput
import kotlin.test.assertEquals

class Day07Test {

    private val path = "07_test"

    @Test
    fun `parse input`() {
        val input = readInput(path, ::mapInput);
        assertEquals(listOf(16, 1, 2, 0, 4, 2, 7, 1, 2, 14), input)
    }

    @Test
    fun `test triangular numbers`() {
        val expectedResults = listOf(0, 1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 66, 78, 91, 105, 120, 136, 153, 171, 190, 210, 231, 253, 276, 300, 325, 351, 378, 406, 435, 465, 496, 528, 561, 595, 630, 666)
        val forInputs = IntRange(0,36).toList()
        assertEquals(expectedResults, forInputs.map(::triangularNumber))
    }

    @Test
    fun `part 1 example`() {
        val input = readInput(path, ::mapInput);
        assertEquals(37, part1(input))
    }

    @Test
    fun `part 2 example`() {
        val input = readInput(path, ::mapInput);
        assertEquals(168, part2(input))
    }
}