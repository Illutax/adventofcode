package tech.dobler.aoc22;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day05Test {

    final String testInput = """
                [D]   \s
            [N] [C]   \s
            [Z] [M] [P]
             1   2   3\s
                        
            move 1 from 2 to 1
            move 3 from 1 to 3
            move 2 from 2 to 1
            move 1 from 1 to 2""";
    private Day05 day;

    @BeforeEach
    void setUp() {
        day = new Day05();
    }

    private static String readInput()
    {
        return Util.readInput(5);
    }

    @Test
    void part1() {
        final var result = day.part1(testInput);

        assertThat(result).isEqualTo("CMZ");

        final var contents = readInput();
        assertThat(day.part1(contents)).isEqualTo("TLNGFGMFN");
    }

    @Test
    void part2() {
        final var result = day.part2(testInput);

        assertThat(result).isEqualTo("MCD");

        final var contents = readInput();
        assertThat(day.part2(contents)).isEqualTo("FGLQJCMBD");
    }
}