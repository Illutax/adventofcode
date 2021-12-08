package day08

import readInput

typealias Input = List<Pair<List<String>, List<String>>>
typealias Output = Int

fun main() {
    val input = readInput("08", ::mapInput)
    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}

/**
 * each part consists of
 *  first = digit encodings
 * and
 *  second = used digits
 */
fun mapInput(lines: Sequence<String>): Input = lines.map { line ->
    line.split(" | ").map { it.split(' ').map { entry -> entry.toCharArray().sorted().joinToString("") } }
}.map { parts ->
    val (a, b) = parts; Pair(a, b)
}.toList()

// Easy hence 1, 4, 7 and 8 are identifiable through the amount of segments used
fun part1(input: Input): Output = input.flatMap { line -> line.second.filter { setOf(2, 3, 4, 7).contains(it.length) } }.count()

enum class Segment {
    TOP, TOP_LEFT, TOP_RIGHT, MIDDLE, BOTTOM_LEFT, BOTTOM_RIGHT, BOTTOM
}

fun decode(encodedDigits: List<String>): Map<String, String> {
    val decodingMap = HashMap<String, String>()
    val lengths = encodedDigits.map { it.length }.toSet()
    // Construct the decoding map in reverse to enhance the lookup while constructing
    if (lengths.contains(2)) decodingMap["1"] = encodedDigits.first { it.length == 2 }
    if (lengths.contains(4)) decodingMap["4"] = encodedDigits.first { it.length == 4 }
    if (lengths.contains(3)) decodingMap["7"] = encodedDigits.first { it.length == 3 }
    if (lengths.contains(7)) decodingMap["8"] = encodedDigits.first { it.length == 7 }

    val setOf235 = encodedDigits.filter { it.length == 5 }
    val setOf069 = encodedDigits.filter { it.length == 6 }

    val segmentLookup = HashMap<Segment, String>()
    // TOP
    if (decodingMap.contains("7") && decodingMap.contains("1"))
        segmentLookup[Segment.TOP] = decodingMap["7"]!! - decodingMap["1"]!!

    // Ξ (\xi)
    if (setOf235.count() == 3)
        decodingMap["Xi"] = setOf235.reduce { a, b -> a.intersection(b) }

    // 3
    if (decodingMap.contains("1") && setOf235.count() == 3)
        decodingMap["3"] = setOf235.first{ decodingMap["1"]!!.toCharArray().all(it::contains) }

    // 1 but on the left side = I
    if (decodingMap.contains("8") && decodingMap.contains("Xi") && decodingMap.contains("1"))
        decodingMap["I"] = decodingMap["8"]!! - decodingMap["Xi"]!! - decodingMap["1"]!!

    // TOP_LEFT
    if (decodingMap.contains("4") && decodingMap.contains("I"))
        segmentLookup[Segment.TOP_LEFT] = decodingMap["4"]!!.intersection(decodingMap["I"]!!)

    // 5
    if (segmentLookup.containsKey(Segment.TOP_LEFT) && setOf235.count() > 0)
        decodingMap["5"] = setOf235.first { digit -> (digit - segmentLookup[Segment.TOP_LEFT]!!) != digit }

    // (0 & 6 & 9) = 5 - MIDDLE
    if (setOf069.count() == 3 && decodingMap.contains("5"))
        segmentLookup[Segment.MIDDLE] = decodingMap["5"]!! - setOf069.reduce { a, b -> a.intersection(b) }

    // 0
    if (decodingMap.contains("8") && segmentLookup.contains(Segment.MIDDLE))
        decodingMap["0"] = decodingMap["8"]!! - segmentLookup[Segment.MIDDLE]!!

    // 2
    if (decodingMap.contains("3") && decodingMap.contains("5") && setOf235.count() == 3)
        decodingMap["2"] = setOf235.first { it != decodingMap["3"]!! && it != decodingMap["5"]!! }

    // 6
    if (decodingMap.contains("1") && setOf069.count() == 3)
        decodingMap["6"] = setOf069.first { it != it.mashTogether(decodingMap["1"]!!) }

    // 9
    if (decodingMap.contains("0") && decodingMap.contains("6") && setOf069.count() == 3)
        decodingMap["9"] = setOf069.first { it != decodingMap["0"]!! && it != decodingMap["6"]!! }

    // I put my thing down, flip it and reverse it
    return decodingMap.entries.associate { (k, v) -> v to k }
}

operator fun String.minus(toBeRemoved: String): String = this.toCharArray().filter { !toBeRemoved.contains(it) }.joinToString("")
fun String.mashTogether(toBeRemoved: String): String = this.toCharArray().union(toBeRemoved.toCharArray().toList()).joinToString("")

/**
 * Intersection realized as difference of difference a ∩ b = a\(a\b)
 */
fun String.intersection(toBeAdded: String): String =
    this.toCharArray().asList().minus(this.toCharArray().asList().minus(toBeAdded.toCharArray().toList())).joinToString("")

fun part2(input: Input): Output = input.map { line -> line.second.map(decode(line.first)::get).joinToString("") }.map(String::toInt).sum()
