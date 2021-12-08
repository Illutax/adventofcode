package day04

import org.junit.jupiter.api.Test
import util.readInput
import kotlin.test.assertEquals

class Day04Test {

    private val path = "04_test"

    @Test
    fun `parse input`() {
        // Arrange
        val expectedNumbers = listOf(7, 4, 9, 5, 11, 17, 23, 2, 0, 14, 21, 24, 10, 16, 13, 6, 15, 25, 12, 22, 18, 20, 8, 19, 3, 26, 1)
        val expectedBoards = listOf(
            listOf(22, 13, 17, 11, 0, 8, 2, 23, 4, 24, 21, 9, 14, 16, 7, 6, 10, 3, 18, 5, 1, 12, 20, 15, 19),
            listOf(3, 15, 0, 2, 22, 9, 18, 13, 17, 5, 19, 8, 7, 25, 23, 20, 11, 10, 24, 4, 14, 21, 16, 12, 6),
            listOf(14, 21, 17, 24, 4, 10, 16, 15, 9, 19, 18, 8, 23, 26, 20, 22, 11, 13, 6, 5, 2, 0, 12, 3, 7)
        )

        // Act
        val input = readInput(path, ::mapInput);

        // Assert
        assertEquals(listOf(25, 25, 25), expectedBoards.map { it.count() }) // sanity check

        assertEquals(expectedNumbers, input.numbers())
        val actualNumbers = input.boards().map { it.numbers() }
        assertEquals(listOf(25, 25, 25), actualNumbers.map { it.count() })
        assertEquals(expectedBoards, actualNumbers)
    }

    @Test
    fun `part 1 example`()
    {
        val input = readInput(path, ::mapInput);
        assertEquals(4512, part1(input))
    }

    @Test
    fun `part 2 example`()
    {
        val input = readInput(path, ::mapInput);
        assertEquals(1924, part2(input))
    }
}