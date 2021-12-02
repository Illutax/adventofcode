package day02

import org.junit.jupiter.api.Test
import readInput
import kotlin.test.assertEquals

class Day02Test {

    private val path = "02_test"

    @Test
    fun `parse input`() {
        val input = readInput(path, ::mapInput);
        assertEquals(
            listOf(
                Operator(Direction.FORWARD, 5),
                Operator(Direction.DOWN, 5),
                Operator(Direction.FORWARD, 8),
                Operator(Direction.UP, 3),
                Operator(Direction.DOWN, 8),
                Operator(Direction.FORWARD, 2),
            ), input.toList()
        )
    }

    @Test
    fun `part 1 example`() {
        val input = readInput(path, ::mapInput);
        assertEquals(150, part1(input))
    }

    @Test
    fun `part 2 example`() {
        val input = readInput(path, ::mapInput);
        assertEquals(900, part2(input))
    }
}