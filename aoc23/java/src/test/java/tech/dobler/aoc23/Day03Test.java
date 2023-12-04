package tech.dobler.aoc23;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class Day03Test {

    final String testInput = """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..
            """;
    private Day03 day;

    @BeforeEach
    void setUp() {
        day = new Day03();
    }

    private static String readInput() {
        return Util.readInput(3);
    }

    @Test
    void part1() {
        assertThat(day.part1(testInput)).isEqualTo(4_361);

        assertThat(day.part1(readInput()))
                .isLessThan(540_380)
                .isLessThan(530_977)
                .isEqualTo(527_446);
    }

    @Test
    void part2() {
        assertThat(day.part2(testInput)).isEqualTo(467_835);

        assertThat(day.part2(readInput())).isEqualTo(73_201_705);
    }
}