package day01

import org.junit.jupiter.api.Test

import readInput
import kotlin.test.assertEquals

class Day01Test {

    private val path = "01_test"

    @Test
    fun `parse input`() {
        val testInput = readInput(path, ::mapInput)
        assertEquals(listOf(199, 200, 208, 210, 200, 207, 240, 269, 260, 263), testInput)
    }

    @Test
    fun `part1 example`()
    {
        val input = readInput(path, ::mapInput)
        assertEquals(7, part1(input))
    }

    @Test
    fun `part2 example`()
    {
        val input = readInput(path, ::mapInput)
        assertEquals(5, part2(input))
    }
}