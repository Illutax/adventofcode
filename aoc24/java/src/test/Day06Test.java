import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import tech.dobler.aoc24.Day06;
import tech.dobler.aoc24.Util;

import static org.assertj.core.api.Assertions.assertThat;

class Day06Test {

    final String testInput = """
            ....#.....
            .........#
            ..........
            ..#.......
            .......#..
            ..........
            .#..^.....
            ........#.
            #.........
            ......#...
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
        assertThat(day.part1(testInput)).isEqualTo(41);

        assertThat(day.part1(readInput())).isEqualTo(4665);
    }

    @Test
    void part2() {
        assertThat(day.part2(testInput)).isEqualTo(6);

        assertThat(day.part2(readInput())).isEqualTo(1688);
    }
}