package tech.dobler.aoc22;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day04Test {

    final String testInput = """
            2-4,6-8
            2-3,4-5
            5-7,7-9
            2-8,3-7
            6-6,4-6
            2-6,4-8""";
    private Day04 day;

    @BeforeEach
    void setUp() {
        day = new Day04();
    }

    private static Stream<String> readInput()
    {
        return Util.readLines(4);
    }

    @Test
    void part1() {
        final var result = day.part1(Util.splitByNewLine(testInput));

        assertThat(result).isEqualTo(2);

        final var contents = readInput();
        assertThat(day.part1(contents)).isEqualTo(507);
    }

    @Test
    void part2() {
        final var result = day.part2(Util.splitByNewLine(testInput));

        assertThat(result).isEqualTo(4);

        final var contents = readInput();
        assertThat(day.part2(contents))
                .isGreaterThan(789)
                .isEqualTo(897);
    }
}