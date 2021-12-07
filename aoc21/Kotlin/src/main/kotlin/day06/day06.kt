package day06

import readInput

typealias Input = List<Int>
typealias Output = Int

fun main() {
    val input = readInput("06", ::mapInput)
    println("Amount of lantern fish: ${input.count()}")

    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}

fun mapInput(lines: Sequence<String>): Input = lines.flatMap { it.split(",").map(String::toInt) }.toList()

fun part1(input: Input): Output = tickNTimes(input, 80).count()

fun tickNTimes(input: Input, times: Int): Input {
    var newInput = input
    repeat(times) { tick(newInput).also { newInput = it } }
    return newInput
}

fun tick(input: List<Int>): List<Int> = input.map { it - 1 }.flatMap { if (it < 0) listOf(6, 8) else listOf(it) }//.sortedWith(::is8)

fun is8(o1: Int, o2: Int) = if (o1 == 8) 1 else if (o2 == 8) -1 else 0

fun part2(input: Input): Long = TODO()