package day07

import getMinMax
import readInput
import kotlin.math.abs

typealias Input = List<Int>
typealias Output = Int

fun main() {
    val input = readInput("07", ::mapInput)
    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}

fun mapInput(lines: Sequence<String>): Input = lines.flatMap { it.split(",").map(String::toInt) }.toList()

fun part1(input: Input): Output {
    val (min, max) = input.getMinMax()
    return IntRange(min + 1, max - 1).minOf { i -> input.sumOf { abs(it - i) } }
}

fun triangularNumber(n: Output): Output = n * (n + 1) / 2

fun part2(input: Input): Output {
    val (min, max) = input.getMinMax()
    return IntRange(min + 1, max - 1).minOf { i -> input.sumOf { triangularNumber(abs(it - i)) } }
}
