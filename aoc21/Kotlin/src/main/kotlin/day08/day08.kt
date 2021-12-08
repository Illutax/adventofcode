package day08

import readInput

typealias Input = List<Pair<List<String>, List<String>>>
typealias Output = Int

fun main() {
    val input = readInput("08", ::mapInput)
    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}

/**
 * each part consists of
 *  first = digit encodings
 * and
 *  second = used digits
 */
fun mapInput(lines: Sequence<String>): Input = lines.map { line -> line.split(" | ").map { it.split(' ') } }.map { parts ->
    val (a, b) = parts; Pair(a, b)
}.toList()

/**
 * Easy hence 1, 4, 7 and 8 are identifiable through the amount of segments used
 */
fun part1(input: Input): Any = input.flatMap { line -> line.second.filter { setOf(2,3,4,7).contains(it.length) } }.count()

fun part2(input: Input): Output {
    TODO()
}
