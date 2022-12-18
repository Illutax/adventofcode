package tech.dobler.aoc22;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day01Test {

    final String testInput = """
            1000
            2000
            3000
                        
            4000
                        
            5000
            6000
                        
            7000
            8000
            9000
                        
            10000""";
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
        final var result = day.part1(testInput);

        assertThat(result).isEqualTo(24_000);

        final var contents = readInput();
        assertThat(day.part1(contents)).isEqualTo(74_198);
    }

    @Test
    void part2() {
        final var result = day.part2(testInput);

        assertThat(result).isEqualTo(45_000);

        final var contents = readInput();
        assertThat(day.part2(contents)).isGreaterThan(74_198);
        assertThat(day.part2(contents)).isEqualTo(209_914);
    }
}