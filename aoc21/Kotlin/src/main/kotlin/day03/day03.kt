package day03

import readInput
import kotlin.math.pow
import kotlin.streams.toList

typealias Input = List<List<Int>>
typealias Output = Int

fun main() {
    val input = readInput("3", ::mapInput)
    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}

fun mapInput(lines: Sequence<String>): Input = lines.map { it -> it.chars().map { it - 48 }.toList() }.toList()

fun part1(input: Input): Output {
    val reportLength = input.first().count()
    val maxValue: Int = (2.0.pow(reportLength) - 1).toInt()

    val gamma = calcGamma(input)
    return gamma * (maxValue - gamma)
}

private fun calcGamma(input: Input): Int = input.reduce { a, b -> a.zip(b) { c, d -> c + d } }
    .map { if (it > input.count() / 2) 1 else 0 }
    .binaryToInt()

fun part2(input: Input): Output = getOxygenRating(input) * getCO2Rating(input)

private fun getOxygenRating(input: Input): Int = getGenericRating(input, 1) { a, b -> a > b }
private fun getCO2Rating(input: Input): Int = getGenericRating(input, 0) { a, b -> a < b }

private fun getGenericRating(input: Input, digitOfInterest: Int, relation: (Int, Int) -> Boolean): Int {
    var list = input;
    var indexToCheck = 0
    while (list.count() > 1) {
        val partitioned = list.partition { it[indexToCheck] != digitOfInterest };
        list = partitioned.second
        if (relation(partitioned.first.count(), partitioned.second.count()))
            list = partitioned.first
        indexToCheck++
    }
    return list.first().binaryToInt()
}

private fun Iterable<Int>.binaryToInt(): Int = this.joinToString(separator = "").toInt(2)
