package tech.dobler.aoc23;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day_multiLineInput_Test {

    final String testInput = """
            """;
    private Day02 day;

    @BeforeEach
    void setUp() {
        day = new Day02();
    }

    private static Stream<String> readInput() {
        return Util.readLines(2);
    }

    @Test
    void part1() {
        assertThat(day.part1(Util.splitByNewLine(testInput))).isEqualTo(-1L);

        assertThat(day.part1(readInput())).isEqualTo(-1L);
    }

    @Disabled
    @Test
    void part2() {
        assertThat(day.part2(Util.splitByNewLine(testInput))).isEqualTo(-1L);

        assertThat(day.part2(readInput())).isEqualTo(-1L);
    }
}