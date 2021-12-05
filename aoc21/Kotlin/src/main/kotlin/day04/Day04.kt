package day04

import readInput

typealias Input = BingoGame
typealias Output = Int

fun main() {
    val game = readInput("04", ::mapInput)
    println("Amount of boards: ${game.boards().count()}")
    part2(game) // part 1 & 2
}

class Board(lines: List<String>) {
    private val numbers: List<Int>
    private val markedNumbers: MutableMap<Int, Boolean>

    init {
        this.numbers = lines.flatMap { it.split(regex = "\\s+".toRegex()).filter(String::isNotEmpty).map(String::toInt) }
        markedNumbers = HashMap()
    }

    fun unmarkedNumbers(): List<Int> = numbers.filter { number -> !markedNumbers.contains(number) }

    fun addNumber(number: Int) {
        if (numbers.contains(number)) markedNumbers[number] = true
    }

    fun checkWinCondition(): Boolean = checkRows() || checkColumns()

    private fun checkRows(): Boolean = numbers.asSequence().chunked(5).any { row -> row.all { cell -> markedNumbers.contains((cell)) } }

    private fun checkColumns(): Boolean = IntProgression.fromClosedRange(0, 4, 1)
        .any { x ->
            IntProgression.fromClosedRange(0, 4, 1)
                .all { y -> markedNumbers.contains(pointToNumber(x, y)) }
        }

    private fun pointToNumber(x: Int, y: Int): Int = numbers[x + y * 5]
    internal fun numbers(): List<Int> = numbers.toList()
    override fun toString(): String = "Board(numbers=$numbers, checkedNumbers=\n${printCheckedNumbers()})"

    private fun printCheckedNumbers(): String {
        val sb = StringBuilder()
        for (y in 0..4) {
            for (x in 0..4)
                sb.append(
                    if (markedNumbers.contains(pointToNumber(x, y))) String.format("[%1$2s]", numbers[x + y * 5]) else String.format(
                        "%1$3s ",
                        numbers[x + y * 5]
                    )
                )
            sb.append('\n')
        }
        return sb.toString()
    }
}

class BingoGame(numbers: String = "", boards: List<List<String>> = listOf()) {
    private val numbers: List<Int>
    private val boards: List<Board>

    init {
        this.numbers = numbers.split(",").map(String::toInt)
        this.boards = boards.map { Board(it.drop(1)) }
    }

    fun numbers(): Iterable<Int> = numbers.asIterable()
    fun boards(): List<Board> = boards.toList()
    override fun toString(): String = "BingoGame(numbers=$numbers, boards=$boards)"
}

fun mapInput(lines: Sequence<String>): Input {
    val trimmed = lines.map(String::trim).toList()
    return BingoGame(trimmed.first(), trimmed.drop(1).chunked(6))
}

internal fun part1(game: Input): Output {
    for (number in game.numbers()) {
        var winningBoard: Board? = null
        val winningBoards: MutableList<Board> = ArrayList()
        for (board in game.boards()) {
            board.addNumber(number)

            if (board.checkWinCondition()) {
                winningBoard = board
                winningBoards.add(board)
            }
        }
        if (winningBoard != null) {
            println("$number; ${winningBoard.unmarkedNumbers().sum()}; ${winningBoard.unmarkedNumbers().sum() * number}; $winningBoard")
            return number * winningBoard.unmarkedNumbers().sum()
        }
    }
    throw IllegalStateException("Should never happen")
}

fun part2(game: Input): Output {
    val winningBoards: MutableList<Board> = ArrayList()
    val winningNumbers: MutableList<Int> = ArrayList();
    for (number in game.numbers()) {
        for (board in game.boards()) {
            if (!winningBoards.contains(board)) {
                board.addNumber(number)
                if (board.checkWinCondition()) {
                    winningBoards.add(board)
                    winningNumbers.add(number)
                }
            }
        }
    }
    assert(winningBoards.count() == winningNumbers.count());

    println("Part1: ${winningNumbers.first() * winningBoards.first().unmarkedNumbers().sum()}")
    println("Part2: ${winningNumbers.last() * winningBoards.last().unmarkedNumbers().sum()}")

    return winningNumbers.last() * winningBoards.last().unmarkedNumbers().sum()
}
