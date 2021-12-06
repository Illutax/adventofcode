package day05

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day05Test {

    private val path = "xx_test"
    private val exampleInput = """"""

    @Test
    fun `parse input`()
    {
        val input = mapInput(exampleInput.lineSequence());
        assertEquals(null as Output, part1(input))
        TODO()
    }

    @Test
    fun `part 1 example`()
    {
        val input = mapInput(exampleInput.lineSequence())
//        val input = readInput(path, ::mapInput);
        assertEquals(Int.MIN_VALUE, part1(input))
        TODO()
    }

    @Test
    fun `part 2 example`()
    {
        val input = mapInput(exampleInput.lineSequence())
//        val input = readInput(path, ::mapInput);
        assertEquals(Int.MIN_VALUE, part2(input))
        TODO()
    }
}