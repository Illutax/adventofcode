package day01

import readInput

typealias Input = List<Int>
typealias Output = Int

fun mapInput(lines: Sequence<String>): Input = lines.map(String::toInt).toList()

fun main() {
    val input = readInput("01", ::mapInput)
    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}

//fun part1(input: Input): Output = input.windowed(2).map { if (it[1]-it[0] > 0) 1 else 0 }.sum()
fun part1(input: Input): Output = input.windowed(2).count { (a, b) -> a < b }

//fun part2(input: Input): Any = input.windowed(3).map { it.sum() }.windowed(2).map { if (it[1]-it[0] > 0) 1 else 0 }.sum()
fun part2(input: Input): Any = input.windowed(4).count { (a, _, _, b) -> a < b } // two middle ones overlap and can be omitted