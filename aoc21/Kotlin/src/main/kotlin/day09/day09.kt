package day09

import util.advent
import java.util.*

typealias Input = List<List<Int>>
typealias Output = Int

fun main() = advent(9, ::mapInput, ::part1, ::part2)

fun mapInput(lines: Sequence<String>): Input = lines.map { it.split("").filter { s -> s != "" }.map(String::toInt) }.toList()

fun part1(grid: Input): Output {
    val maxX = grid[0].count() - 1
    val maxY = grid.count() - 1
    val localMinima = LinkedList<Int>()
    for (y in 0..maxY) {
        for (x in 0..maxX) {
            val cellValue = grid[y][x]
            if (lowestNeighbours(grid, x, y, maxX, maxY) == Pair(x,y)) {
                localMinima.add(cellValue + 1)
                print(".")
            }
            else
                print(cellValue)
        }
        println()
    }
    return localMinima.sum();
}


@Suppress("unused")
fun neighbours(grid: List<List<Int>>, x: Int, y: Int, maxX: Int, maxY: Int): Set<Pair<Pair<Int, Int>, Int>> {
    val neighbours = HashSet<Pair<Pair<Int, Int>, Int>>()
    for (j in y - 1..y + 1)
        for (i in x - 1..x + 1)
            if (j in 0..maxY && i in 0..maxX)
                neighbours.add(Pair(Pair(j, i), grid[j][i]))
    return neighbours;
}

fun lowestNeighbours(grid: List<List<Int>>, x: Int, y: Int, maxX: Int, maxY: Int): Pair<Int, Int>? {
    val neighbours = HashSet<Pair<Pair<Int, Int>, Int>>()
    for (j in y - 1..y + 1)
        for (i in x - 1..x + 1)
            if (j in 0..maxY && i in 0..maxX && (j == y || i == x))
                neighbours.add(Pair(Pair(i, j), grid[j][i]))
    val (lowest, secondLowest) = neighbours.sortedBy { it.second }.take(2)
    return if (lowest.second == secondLowest.second) null else lowest.first
}

fun part2(input: Input): Any {
    TODO()
}
