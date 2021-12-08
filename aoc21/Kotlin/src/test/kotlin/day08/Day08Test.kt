package day08

import org.junit.jupiter.api.Test
import readInput
import kotlin.test.assertEquals

class Day08Test {

    private val path = "08_test"

    @Test
    fun `parse input`()
    {
        val input = readInput(path, ::mapInput);
        assertEquals(null as Input, input)
    }

    @Test
    fun `part 1 example`()
    {
        val input = readInput(path, ::mapInput);
        assertEquals(Int.MIN_VALUE, part1(input))
        TODO()
    }

    @Test
    fun `part 2 example`()
    {
        val input = readInput(path, ::mapInput);
        assertEquals(Int.MIN_VALUE, part2(input))
        TODO()
    }
}