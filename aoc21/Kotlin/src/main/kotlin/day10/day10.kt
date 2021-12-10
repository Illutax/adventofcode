package day10

import util.advent
import util.median
import java.util.*

typealias Input = List<List<Char>>
typealias Output1 = Int
typealias Output2 = Long

fun main() = advent(10, ::mapInput, ::part1, ::part2)

fun mapInput(lines: Sequence<String>): Input = lines.map { it.toList() }.toList()

val closingChars: List<Char> = listOf(')', ']', '}', '>')
val openingChars: List<Char> = listOf('(', '[', '{', '<')
val corruptedScores: List<Int> = listOf(3, 57, 1197, 25137)

fun part1(input: Input): Output1 = findCorruptedAndIncomplete(input).first.sumOf(::getCorruptedScore)

fun findCorruptedAndIncomplete(input: Input): Pair<List<Char>, List<Iterable<Pair<Char, Int>>>> {
    val incomplete = ArrayList<Iterable<Pair<Char, Int>>>()
    val corrupted = ArrayList<Char>()
    input.forEach { line ->
        val stack = Stack<Char>()
        var isCorrupted = false;
        for (char in line) {
            if (openingChars.contains(char)) {
                stack.push(char)
                continue
            }
            if (stack.empty()) {
                throw IllegalStateException("Unexpected closing char $char")
            }
            if (char == oppositeOf(stack.peek())) stack.pop()
            else {
                isCorrupted = true;
                corrupted.add(char)
                println("Expected ${oppositeOf(stack.peek())}, but found $char instead.")
                break
            }
        }
        if (!isCorrupted)
            incomplete.add(stack.reversed().map { Pair(oppositeOf(it), 0) })
    }
    return Pair(corrupted, incomplete)
}

fun getCorruptedScore(c: Char): Int = corruptedScores[closingChars.indexOf(c)]
fun getIncompleteScore(c: Char): Long = closingChars.indexOf(c) + 1L

fun oppositeOf(c: Char): Char {
    if (closingChars.contains(c)) return openingChars[closingChars.indexOf(c)]
    if (openingChars.contains(c)) return closingChars[openingChars.indexOf(c)]
    throw IllegalStateException("Should never happen")
}

fun part2(input: Input): Output2 =
    findCorruptedAndIncomplete(input).second.map {
        it.fold(Pair('0', 0L)) { a, b -> Pair(b.first, Math.addExact(Math.multiplyExact(a.second, 5L), getIncompleteScore(b.first))) }
    }.map(Pair<Char, Long>::second).sorted().median()
