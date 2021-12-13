package day11

import util.advent

//typealias Input = List<Int>
typealias Input = List<MutableList<Int>>

typealias Output = Int

fun main() = advent(11, ::mapInput, ::part1, ::part2)

fun mapInput(lines: Sequence<String>): Input = lines.map { it.toList().map { c -> c.code - 48 }.toMutableList() }.toList()

fun part1(grid: Input): Any {
    val counter = Counter()
    repeat(2) { calculateStep(grid, counter) }
    return counter
}

data class Counter(var value: Int = 0) {
    fun incrementAndGet(d: Int = 1): Int {
        value += d
        return value
    }

    fun get(): Int = value

    override fun toString(): String = value.toString()
}

fun calculateStep(grid: Input, i: Counter): Counter {
    incrementAll(grid)
    resetFlashes(grid, i)
    return i
}

fun incrementAll(grid: List<MutableList<Int>>) = grid.forEachIndexed { iY, line ->
    line.forEachIndexed { iX, _ ->
        increaseOneAndCheckForSpark(grid, iX, iY)
    }
}

fun flash(grid: List<MutableList<Int>>, x: Int, y: Int, counter: Counter) {
    grid[y][x] = -1
    counter.incrementAndGet()
    for (dy in -1..1)
        for (dx in -1..1) {
            val xx = x + dx
            val yy = y + dy
            if (0 <= xx && xx < grid[0].count() && 0 <= yy && yy < grid.count() && grid[yy][xx] != -1) {
                grid[yy][xx]++
                if (grid[yy][xx] > 9) flash(grid, xx, yy, counter)
            }
        }
}

fun increaseOneAndCheckForSpark(grid: List<MutableList<Int>>, x: Int, y: Int) {
    if (grid[y][x] > 9) return

    grid[y][x] += 1
    if (grid[y][x] > 9) {
        println("Sparked at $x:$y")
        clampedIntRange(y).forEachIndexed { dy, _ ->
            clampedIntRange(x).forEachIndexed { dx, _ -> if (dx != x && dy != y) increaseOneAndCheckForSpark(grid, dx, dy) }
        }
    }
}

private fun clampedIntRange(i: Int): IntRange = IntRange((i - 1).coerceAtLeast(0), (i + 1).coerceAtMost(9))

fun resetFlashes(grid: Input, c: Counter) = grid.forEach { line ->
    line.forEachIndexed { index, it ->
        if (it > 9) {
            line[index] = 0
            c.incrementAndGet()
        }
    }
}

fun part2(input: Input): Output {
    TODO()
}
