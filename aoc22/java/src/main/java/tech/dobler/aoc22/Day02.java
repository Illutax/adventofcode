package tech.dobler.aoc22;

import java.util.Map;
import java.util.stream.Stream;

public class Day02 {
    // <Enemy> <Me>
    // A, X = Rock      = 1
    // B, Y = Paper     = 2
    // C, Z = Scissors  = 3

    // Lose = 0
    // Draw = 3
    // Win  = 6
    @SuppressWarnings("PointlessArithmeticExpression")
    static final Map<String, Integer> scoresPart1 = Map.of(
            "A Y", 2 + 6,
            "B Y", 2 + 3,
            "C Y", 2 + 0,

            "A X", 1 + 3,
            "B X", 1 + 0,
            "C X", 1 + 6,

            "A Z", 3 + 0,
            "B Z", 3 + 6,
            "C Z", 3 + 3
    );

    // <Enemy> <Me>
    // A = Rock      = 1
    // B = Paper     = 2
    // C = Scissors  = 3

    // X = Lose
    // Y = Draw
    // Z = Win
    @SuppressWarnings("PointlessArithmeticExpression")
    static final Map<String, Integer> scoresPart2 = Map.of(
            "A X", 3 + 0,
            "B X", 1 + 0,
            "C X", 2 + 0,

            "A Y", 1 + 3,
            "B Y", 2 + 3,
            "C Y", 3 + 3,

            "A Z", 2 + 6,
            "B Z", 3 + 6,
            "C Z", 1 + 6
    );

    public int part1(Stream<String> input) {
        return input
                .mapToInt(round -> scoresPart1.getOrDefault(round, -10_000))
                .sum();
    }

    public int part2(Stream<String> input) {
        return input
                .mapToInt(round -> scoresPart2.getOrDefault(round, -10_000))
                .sum();
    }
}