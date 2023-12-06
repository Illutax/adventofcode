package tech.dobler.aoc23;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day_singleLineInput_Test {

    final String testInput = """
            """;
    private Day03 day;

    @BeforeEach
    void setUp() {
        day = new Day03();
    }

    private static String readInput() {
        return Util.readInput(1);
    }

    @Test
    void part1() {
        assertThat(day.part1(testInput)).isEqualTo(-1L);

        assertThat(day.part1(readInput())).isEqualTo(-1L);
    }

    @Disabled
    @Test
    void part2() {
        assertThat(day.part2(testInput)).isEqualTo(-1L);

        assertThat(day.part2(readInput())).isEqualTo(-1L);
    }

}