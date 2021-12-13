package day11

import org.junit.jupiter.api.Test
import util.readInput
import kotlin.test.assertEquals

class Day11Test {

    private val path = "11_test"

    @Test
    fun `parse input`() {
        val input = readInput(path, ::mapInput);
        assertEquals(
            listOf(
                listOf(5, 4, 8, 3, 1, 4, 3, 2, 2, 3),
                listOf(2, 7, 4, 5, 8, 5, 4, 7, 1, 1),
                listOf(5, 2, 6, 4, 5, 5, 6, 1, 7, 3),
                listOf(6, 1, 4, 1, 3, 3, 6, 1, 4, 6),
                listOf(6, 3, 5, 7, 3, 8, 5, 4, 7, 8),
                listOf(4, 1, 6, 7, 5, 2, 4, 6, 4, 5),
                listOf(2, 1, 7, 6, 8, 4, 1, 7, 2, 1),
                listOf(6, 8, 8, 2, 8, 8, 1, 1, 3, 4),
                listOf(4, 8, 4, 6, 8, 4, 8, 5, 5, 4),
                listOf(5, 2, 8, 3, 7, 5, 1, 5, 2, 6)
            ), input
        )
    }

    @Test
    fun `part 1 example`() {
        val input = readInput(path, ::mapInput);
        assertEquals(1_656, part1(input))
    }

    @Test
    fun `part 2 example`() {
        val input = readInput(path, ::mapInput);
        assertEquals(Int.MIN_VALUE, part2(input))
    }
}