import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import tech.dobler.aoc24.Day06;
import tech.dobler.aoc24.Util;

import static org.assertj.core.api.Assertions.assertThat;

class Day_singleLineInput_Test {

    final String testInput = """
            """;
    private Day06 day;

    @BeforeEach
    void setUp() {
        day = new Day06();
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