package tech.dobler.aoc22;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day03Test {

    final String testInput = """
            vJrwpWtwJgWrhcsFMMfFFhFp
            jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
            PmmdzqPrVvPwwTWBwg
            wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
            ttgJtRGJQctTZtZT
            CrZsJsPPZsGzwwsLwLmpwMDw""";
    private Day03 day;

    @BeforeEach
    void setUp() {
        day = new Day03();
    }

    private static Stream<String> readInput()
    {
        return Util.readLines(3);
    }

    @Test
    void part1() {
        final var result = day.part1(Util.splitByNewLine(testInput));

        assertThat(result).isEqualTo(157);

        final var contents = readInput();
        assertThat(day.part1(contents)).isEqualTo(7_763);
    }

    @Test
    void part2() {
        final var result = day.part2(testInput);

        assertThat(result).isEqualTo(70);

        final var contents = Util.readInput(3);
        assertThat(day.part2(contents)).isEqualTo(2_569);
    }
}