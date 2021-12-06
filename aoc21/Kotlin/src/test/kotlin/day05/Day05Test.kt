package day05

import org.junit.jupiter.api.Test
import readInput
import kotlin.test.assertEquals

class Day05Test {

    private val path = "05_test"

    @Test
    fun `parse input`()
    {
        // Arrange
        val isParallelToAxesFun: (Interval) -> Boolean = { it.from.first == it.to.first || it.from.second == it.to.second }
        val isDiagonalFun: (Interval) -> Boolean = { !isParallelToAxesFun(it) }

        // Act
        val input = readInput(path, ::mapInput);

        // Assert
        assertEquals(10, input.count())
        assertEquals(6, input.count(isParallelToAxesFun))
        assertEquals(4, input.count(isDiagonalFun))
        assertEquals(Interval(listOf(0,9,5,9)), input.first())
        assertEquals(Interval(listOf(5,5,8,2)), input.last())
    }

    @Test
    fun `part 1 example`()
    {
        val input = readInput(path, ::mapInput);
        assertEquals(5, part1(input))
    }

    @Test
    fun `part 2 example`()
    {
        val input = readInput(path, ::mapInput);
        assertEquals(12, part2(input))
    }
}