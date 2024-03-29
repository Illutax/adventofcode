package day02

import util.readInput
import model.Point

typealias Input = List<Operator>
typealias Output = Int

enum class Direction {
    FORWARD, UP, DOWN
}

data class Operator(val dir: Direction, val amount: Int) {
    init {
        require(amount >= 0) { "${::amount.name} can't be negative" }
    }
}

fun main() {
    val input = readInput("02", ::mapInput)
    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}

fun mapInput(lines: Sequence<String>): Input = lines.map { it.split(" ") }
    .map { Operator(Direction.valueOf(it[0].uppercase()), it[1].toInt()) }.toList()

private fun parseOperator(it: Operator) = when (it.dir) {
    Direction.FORWARD -> Point(x = it.amount)
    Direction.DOWN -> Point(y = it.amount)
    Direction.UP -> Point(y = -it.amount)
}

fun part1(input: Input): Output = input.asSequence().map(::parseOperator).reduce(Point::add).innerProd()

fun part2(input: Input): Output
{
    var currentAim = 0
    var result = Point()
    input.map(::parseOperator).forEach {
        currentAim += it.y;
        result = result.add(Point(it.x, it.x * currentAim))
    }
    return result.innerProd()
}
