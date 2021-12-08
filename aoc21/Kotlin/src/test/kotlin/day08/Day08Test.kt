package day08

import org.junit.jupiter.api.Test
import readInput
import kotlin.test.assertEquals

class Day08Test {

    private val path = "08_test"

    @Test
    fun `parse input`() {
        val input = readInput(path, ::mapInput);
        assertEquals(
            listOf(
                Pair(listOf("be", "abcdefg", "bcdefg", "acdefg", "bceg", "cdefg", "abdefg", "bcdef", "abcdf", "bde"), listOf("abcdefg", "bcdef", "bcdefg", "bceg")),
                Pair(listOf("abdefg", "bcdeg", "bcg", "cg", "abcdefg", "bdefg", "abcdfg", "abcde", "bcdefg", "cefg"), listOf("bcdefg", "bcg", "abcdefg", "cg")),
                Pair(listOf("abdefg", "cg", "abcde", "abdfg", "abcdfg", "bcdefg", "abcdg", "acfg", "bcg", "abcdefg"), listOf("cg", "cg", "abcdfg", "bcg")),
                Pair(listOf("bcdefg", "bcd", "abcdef", "abdeg", "abcf", "bc", "acdef", "abcde", "acdefg", "abcdefg"), listOf("abcdef", "abcde", "acdefg", "bc")),
                Pair(listOf("abcdefg", "bfg", "fg", "abefg", "abdef", "cefg", "abceg", "abcefg", "abcdeg", "abcdfg"), listOf("cefg", "abcdefg", "bfg", "abefg")),
                Pair(listOf("abefg", "ac", "abcefg", "abcdefg", "acdefg", "bcdfg", "abce", "abdefg", "abcfg", "acf"), listOf("abcdefg", "abce", "ac", "abcdefg")),
                Pair(listOf("bcdfg", "dfg", "abcdefg", "cefg", "abdefg", "abcdef", "bcdef", "abcdg", "bcdefg", "fg"), listOf("cefg", "bcdef", "cefg", "abcdefg")),
                Pair(listOf("bcdefg", "abcefg", "bcefg", "acdefg", "abcdg", "de", "bdef", "cde", "abcdefg", "bcdeg"), listOf("de", "abcefg", "abcdg", "bcefg")),
                Pair(listOf("abdefg", "bcdefg", "cdeg", "abcef", "bcg", "abcdefg", "cg", "abcdfg", "bdefg", "bcefg"), listOf("abcdefg", "bcg", "cg", "bcg")),
                Pair(listOf("abcfg", "cfg", "abcdefg", "abceg", "fg", "abcdeg", "aefg", "abcefg", "abcdf", "bcdefg"), listOf("aefg", "abcfg", "fg", "abceg")))
            ,
            input
        )
    }

    @Test
    fun `decode 1`() {
        assertEquals(mapOf(Pair(encoded1, "1")), decode(listOf(encoded1)))
    }

    @Test
    fun `decode 4`() {
        assertEquals(mapOf(Pair(encoded4, "4")), decode(listOf(encoded4)))
    }

    @Test
    fun `decode 7`() {
        assertEquals(mapOf(Pair("dab", "7")), decode(listOf("dab")))
    }

    @Test
    fun `decode 8`() {
        assertEquals(mapOf(Pair("acedgfb", "8")), decode(listOf("acedgfb")))
    }

    @Test
    fun `abcdef - db results in acef`() {
        assertEquals("acef", "abcdef" - "db")
    }

    @Test
    fun `abc ∩ bcd results in bd`() {
        assertEquals("bc", "abc".intersection("bcd"))
    }

    private val encoded0 = "acedgb" // ✔
    private val encoded1 = "ab" // ✔
    private val encoded2 = "gcdfa"
    private val encoded3 = "cdfab"
    private val encoded4 = "eafb" // ✔
    private val encoded5 = "cdfbe"
    private val encoded6 = "cdfgeb"
    private val encoded7 = "dab" // ✔
    private val encoded8 = "acedgfb" // ✔
    private val encoded9 = "cefabd"

    @Test
    fun `given 2, 3 and 5 (all undecoded) and 1 decodes 3`() {
        assertEquals(
            mapOf(
                Pair(encoded3, "3"),
                Pair(encoded1, "1"),
                Pair("cdf", "Xi"),
            ),
            decode(listOf(encoded1, encoded2, encoded3, encoded5))
        )
    }

    @Test
    fun `given all decodes all`() {
        assertEquals(
            mapOf(
                Pair(encoded0, "0"), // ✔
                Pair(encoded1, "1"), // ✔
                Pair(encoded2, "2"), // ✔
                Pair(encoded3, "3"), // ✔
                Pair(encoded4, "4"), // ✔
                Pair(encoded5, "5"), // ✔
                Pair(encoded6, "6"), // ✔
                Pair(encoded7, "7"), // ✔
                Pair(encoded8, "8"), // ✔
                Pair(encoded9, "9"), // ✔
                Pair("cdf", "Xi"),
                Pair("eg", "I"),
            ),
            decode(listOf(encoded0, encoded1, encoded2, encoded3, encoded4, encoded5, encoded6, encoded7, encoded8, encoded9))
        )
    }

    @Test
    fun `part 1 example`() {
        val input = readInput(path, ::mapInput);
        assertEquals(26, part1(input))
    }

    @Test
    fun `part 2 example`() {
        val input = readInput(path, ::mapInput);
        assertEquals(61_229, part2(input))
    }
}