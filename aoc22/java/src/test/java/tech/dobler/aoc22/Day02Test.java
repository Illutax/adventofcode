package tech.dobler.aoc22;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day02Test {

    final String testInput = """
            A Y
            B X
            C Z""";
    private Day02 day;

    @BeforeEach
    void setUp() {
        day = new Day02();
    }

    private static Stream<String> readInput()
    {
        return Util.readLines(2);
    }

    @Test
    void part1() {
        final var result = day.part1(Util.splitByNewLine(testInput));

        assertThat(result).isEqualTo(15);

        final var contents = readInput();
        assertThat(day.part1(contents)).isEqualTo(8_890);
    }

    @Test
    void part2() {
        final var result = day.part2(Util.splitByNewLine(testInput));

        assertThat(result).isEqualTo(12);

        final var contents = readInput();
        assertThat(day.part2(contents)).isEqualTo(10_238);
    }
}