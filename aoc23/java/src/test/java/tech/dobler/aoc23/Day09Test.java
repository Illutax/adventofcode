package tech.dobler.aoc23;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day09Test {

    final String testInput = """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
            """;
    private Day09 day;

    @BeforeEach
    void setUp() {
        day = new Day09();
    }

    private static Stream<String> readInput() {
        return Util.readLines(9);
    }

    @Test
    void part1() {
        assertThat(day.part1(Util.splitByNewLine(testInput))).isEqualTo(114L);

        assertThat(day.part1(readInput())).isEqualTo(2_098_530_125L);
    }

    @Test
    void part2() {
        assertThat(day.part2(Util.splitByNewLine(testInput))).isEqualTo(2L);

        assertThat(day.part2(readInput())).isEqualTo(1_016L);
    }
}