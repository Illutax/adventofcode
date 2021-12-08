package day06

import util.readInput

typealias Input = List<Int>
typealias Output = Long

fun main() {
    val input = readInput("06", ::mapInput)
    println("Amount of lantern fish: ${input.count()}")

    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}

fun mapInput(lines: Sequence<String>): Input = lines.flatMap { it.split(",").map(String::toInt) }.toList()

fun part1(input: Input): Output = tickNTimes(input, 80)

fun part2(input: Input): Output = tickNTimes(input, 256)

fun tickNTimes(input: List<Int>, times: Int): Output {
    var newInput = input.groupBy { it }.mapValues { it.value.count().toLong() }
    repeat(times) { tick(newInput).also { newInput = it } }
    return newInput.values.fold(0L) { a: Output, b: Output -> Math.addExact(a, b) }
}

fun tick(input: Map<Int, Output>): HashMap<Int, Output> {
    val newMap = HashMap<Int, Output>()
    for (k in input.keys) {
        if (k == 0) {
            newMap.merge(6, input[0]!!) { _, v -> v + (input[0]!!) }
            newMap[8] = input[0]!!
        } else
            newMap[k - 1] = newMap.getOrDefault(k - 1, 0) + input[k]!!
    }
    return newMap
}
