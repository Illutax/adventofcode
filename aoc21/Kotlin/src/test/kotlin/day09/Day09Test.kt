package day09

import org.junit.jupiter.api.Test
import util.readInput
import kotlin.test.assertEquals

class Day09Test {

    private val path = "_test"

    @Test
    fun `parse input`()
    {
        val input = readInput(path, ::mapInput);
        assertEquals(listOf(), input)
    }

    @Test
    fun `part 1 example`()
    {
        val input = readInput(path, ::mapInput);
        assertEquals(Int.MIN_VALUE, part1(input))
    }

    @Test
    fun `part 2 example`()
    {
        val input = readInput(path, ::mapInput);
        assertEquals(Int.MIN_VALUE, part2(input))
    }
}