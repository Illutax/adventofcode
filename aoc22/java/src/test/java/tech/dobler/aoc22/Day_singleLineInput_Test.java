package tech.dobler.aoc22;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day_singleLineInput_Test {

    final String testInput = """
            """;
    private Day01 day;

    @BeforeEach
    void setUp() {
        day = new Day01();
    }

    private static String readInput() {
        return Util.readInput(1);
    }

    @Test
    void part1() {

        assertThat(day.part1(testInput)).isEqualTo(-1);

        assertThat(day.part1(readInput())).isEqualTo(-1);
    }

    @Disabled
    @Test
    void part2() {

        assertThat(day.part2(testInput)).isEqualTo(-1);

        assertThat(day.part2(readInput())).isEqualTo(-1);
    }
}