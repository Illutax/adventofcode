package day09

import org.junit.jupiter.api.Test
import util.readInput
import kotlin.test.assertEquals

class Day09Test {

    private val path = "09_test"

    @Test
    fun `parse input`()
    {
        val input = readInput(path, ::mapInput);
        assertEquals(
            listOf(
                listOf(2, 1, 9, 9, 9, 4, 3, 2, 1, 0),
                listOf(3, 9, 8, 7, 8, 9, 4, 9, 2, 1),
                listOf(9, 8, 5, 6, 7, 8, 9, 8, 9, 2),
                listOf(8, 7, 6, 7, 8, 9, 6, 7, 8, 9),
                listOf(9, 8, 9, 9, 9, 6, 5, 6, 7, 8)), input)
    }

    @Test
    fun `part 1 example`()
    {
        val input = readInput(path, ::mapInput);
        assertEquals(15, part1(input))
    }

    @Test
    fun `part 2 example`()
    {
        val input = readInput(path, ::mapInput);
        assertEquals(1134, part2(input))
    }
}