package tech.dobler.aoc23;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day07Test {

    final String testInput = """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
            """;
    private Day07 day;

    @BeforeEach
    void setUp() {
        day = new Day07();
    }

    private static Stream<String> readInput() {
        return Util.readLines(7);
    }

    @Test
    void part1() {
        assertThat(day.part1(Util.splitByNewLine(testInput))).isEqualTo(6440L);

        assertThat(day.part1(readInput())).isEqualTo(253_603_890L);
    }

    @Disabled
    @Test
    void part2() {
        assertThat(day.part2(Util.splitByNewLine(testInput))).isEqualTo(-1);

        assertThat(day.part2(readInput()))
                .isEqualTo(-1L);
    }
}