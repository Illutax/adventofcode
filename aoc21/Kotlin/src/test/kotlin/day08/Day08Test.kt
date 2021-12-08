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
        assertEquals(
            listOf(
                Pair(
                    listOf("be", "cfbegad", "cbdgef", "fgaecd", "cgeb", "fdcge", "agebfd", "fecdb", "fabcd", "edb"),
                    listOf("fdgacbe", "cefdb", "cefbgd", "gcbe")
                ),
                Pair(
                    listOf("edbfga", "begcd", "cbg", "gc", "gcadebf", "fbgde", "acbgfd", "abcde", "gfcbed", "gfec"),
                    listOf("fcgedb", "cgb", "dgebacf", "gc")
                ),
                Pair(
                    listOf("fgaebd", "cg", "bdaec", "gdafb", "agbcfd", "gdcbef", "bgcad", "gfac", "gcb", "cdgabef"),
                    listOf("cg", "cg", "fdcagb", "cbg")
                ),
                Pair(
                    listOf("fbegcd", "cbd", "adcefb", "dageb", "afcb", "bc", "aefdc", "ecdab", "fgdeca", "fcdbega"),
                    listOf("efabcd", "cedba", "gadfec", "cb")
                ),
                Pair(
                    listOf("aecbfdg", "fbg", "gf", "bafeg", "dbefa", "fcge", "gcbea", "fcaegb", "dgceab", "fcbdga"),
                    listOf("gecf", "egdcabf", "bgf", "bfgea")
                ),
                Pair(
                    listOf("fgeab", "ca", "afcebg", "bdacfeg", "cfaedg", "gcfdb", "baec", "bfadeg", "bafgc", "acf"),
                    listOf("gebdcfa", "ecba", "ca", "fadegcb")
                ),
                Pair(
                    listOf("dbcfg", "fgd", "bdegcaf", "fgec", "aegbdf", "ecdfab", "fbedc", "dacgb", "gdcebf", "gf"),
                    listOf("cefg", "dcbef", "fcge", "gbcadfe")
                ),
                Pair(
                    listOf("bdfegc", "cbegaf", "gecbf", "dfcage", "bdacg", "ed", "bedf", "ced", "adcbefg", "gebcd"),
                    listOf("ed", "bcgafe", "cdgba", "cbgef")
                ),
                Pair(
                    listOf("egadfb", "cdbfeg", "cegd", "fecab", "cgb", "gbdefca", "cg", "fgcdab", "egfdb", "bfceg"),
                    listOf("gbdfcae", "bgc", "cg", "cgb")
                ),
                Pair(
                    listOf("gcafb", "gcf", "dcaebfg", "ecagb", "gf", "abcdeg", "gaef", "cafbge", "fdbac", "fegbdc"),
                    listOf("fgae", "cfgab", "fg", "bagce")
                )
            ),
            input
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
        assertEquals(Int.MIN_VALUE, part2(input))
        TODO()
    }
}