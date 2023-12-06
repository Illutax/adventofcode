package tech.dobler.aoc23;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day06Test {

    final String testInput = """
            Time:      7  15   30
            Distance:  9  40  200
            """;
    private Day06 day;

    @BeforeEach
    void setUp() {
        day = new Day06();
    }

    private static String readInput() {
        return Util.readInput(6);
    }

    @Test
    void part1() {
        assertThat(day.part1(testInput)).isEqualTo(288L);

        assertThat(day.part1(readInput())).isEqualTo(140_220L);
    }

    @Test
    void part2() {
        assertThat(day.part2(testInput)).isEqualTo(71_503);

        assertThat(day.part2(readInput())).isEqualTo(39_570_185L);
    }

}