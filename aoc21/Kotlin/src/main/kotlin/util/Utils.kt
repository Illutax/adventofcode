package util

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.useLines

fun <I, O> advent(day: Int, mapFunction: (Sequence<String>) -> I, part1: (I) -> O, part2: (I) -> O) {
    val input = readInput(day.toString(), mapFunction)
    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}

/**
 * Reads lines from the given input txt file.
 */
fun readInputRaw(name: String) = File("src", "$name.txt").readLines()

/**
 * Reads lines from the given input txt file
 */
fun <T> readInput(name: String, block: (Sequence<String>) -> T) = Path("../input", "day${format(name)}.txt").useLines(block = block)

fun format(name: String): String = if (name.length == 1) "0$name" else name

/**
 * Converts string to util.md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

fun <T : Comparable<T>> Iterable<T>.minMax(): Pair<T, T> = Pair(minOrNull()!!, maxOrNull()!!)