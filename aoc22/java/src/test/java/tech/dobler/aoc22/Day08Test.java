package tech.dobler.aoc22;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day08Test {

    final String testInput = """
            30373
            25512
            65332
            33549
            35390""";
    private Day08 day;

    @BeforeEach
    void setUp() {
        day = new Day08();
    }

    private static Stream<String> readInput() {
        return Util.readLines(8);
    }

    @Test
    void part1() {
        assertThat(day.part1(Util.splitByNewLine(testInput))).isEqualTo(21);

        assertThat(day.part1(readInput())).isEqualTo(1_703);
    }

    @Test
    void part2() {
        assertThat(day.part2(Util.splitByNewLine(testInput))).isEqualTo(8);

        assertThat(day.part2(readInput())).isEqualTo(496_650);
    }
}