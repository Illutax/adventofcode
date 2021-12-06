package day05

import readInput
import kotlin.math.abs

typealias Input = List<Interval>
typealias Output = Int

data class Interval(val coords: List<Int>) {
    val from: Pair<Int, Int>
    val to: Pair<Int, Int>

    init {
        var (fromX, fromY, toX, toY) = coords
        if (fromX == toX && fromY > toY) {
            fromY = toY.also { toY = fromY }
        } else if (fromY == toY && fromX > toX) {
            fromX = toX.also { toX = fromX }
        }
        from = Pair(fromX, fromY)
        to = Pair(toX, toY)
    }

    override fun toString(): String = "Interval[$from -> $to]"
}

fun main() {
    val input = readInput("05", ::mapInput)
    println("part1: ${part1(input)}")
    assert(part1(input) == 4826)
    println("part2: ${part2(input)}")
}

fun mapInput(input: Sequence<String>): List<Interval> =
    input.map { it -> Interval(it.split(" -> ").flatMap { it.split(",") }.map(String::toInt)) }.toList()

fun part1(input: Input): Any {
    val grid: MutableMap<Pair<Int, Int>, Int> = HashMap()
    addIntervalsParallelToAxes(input.filter { it.from.first == it.to.first || it.from.second == it.to.second }, grid)

    render(grid)
    return grid.filter { e -> e.value > 1 }.count()
}

private fun addIntervalsParallelToAxes(intervals: Collection<Interval>, grid: MutableMap<Pair<Int, Int>, Int>) = intervals.forEach {
    assert(it.from.first == it.to.first || it.from.second == it.to.second) { "Should be parallel but wasn't: $it" }

    IntRange(it.from.second, it.to.second).forEach { y ->
        IntRange(it.from.first, it.to.first).forEach { x -> grid.merge(Pair(x, y), 1) { _, value -> value + 1 } }
    }
}

fun part2(input: Input): Output {
    val grid: MutableMap<Pair<Int, Int>, Int> = HashMap()
    val intervalsParallelToAxes = input.filter { it.from.first == it.to.first || it.from.second == it.to.second }.toSet()
    addIntervalsParallelToAxes(intervalsParallelToAxes, grid)
    val diagonalIntervals = input.filter { !intervalsParallelToAxes.contains(it) }
    addCrossingIntervals(diagonalIntervals, grid)

    render(grid)
    return grid.filter { e -> e.value > 1 }.count()
}

fun addCrossingIntervals(intervals: List<Interval>, grid: MutableMap<Pair<Int, Int>, Int>) = intervals.forEach {
    val dx = it.to.first - it.from.first
    val dy = it.to.second - it.from.second
    val unit = Pair(dx/abs(dx), dy/abs(dy))
    assert(abs(dx) == abs(dy)) { "dx: |$dx| should be equal to dy: |$dy|for $it" }

    for (i in IntRange(0, abs(dx)))
        grid.merge(Pair(it.from.first, it.from.second) + (unit * i), 1) { _, value -> value + 1 }
}

private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> = Pair(this.first + other.first, this.second + other.second)
private operator fun Pair<Int, Int>.times(other: Int): Pair<Int, Int> = Pair(this.first * other, this.second * other)

private fun render(grid: Map<Pair<Int, Int>, Int>) {
    val xs = grid.keys.map { it.first }.sorted()
    val ys = grid.keys.map { it.second }.sorted()

    val sb = StringBuilder()
    IntRange(ys.first(), ys.last()).forEach { y ->
        IntRange(xs.first(), xs.last()).forEach { x ->
            val c = grid[Pair(x, y)] ?: -1
            sb.append(if (c == -1) "." else c)
        }
        sb.append('\n')
    }
    print(sb.toString())
}
