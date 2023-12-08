package tech.dobler.aoc23;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day08Test {

    final String testInput1 = """
            RL
                        
            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)
            """;

    final String testInput = """
            LLR
                        
            AAA = (BBB, BBB)
            BBB = (AAA, ZZZ)
            ZZZ = (ZZZ, ZZZ)
            """;
    private Day08 day;

    @BeforeEach
    void setUp() {
        day = new Day08();
    }

    private static String readInput() {
        return Util.readInput(8);
    }

    @Test
    void part1_test1() {
        assertThat(day.part1(testInput1)).isEqualTo(2L);
    }

    @Test
    void part1() {
        assertThat(day.part1(testInput)).isEqualTo(6);

        assertThat(day.part1(readInput())).isEqualTo(14_893L);
    }

    @Disabled
    @Test
    void part2() {
        assertThat(day.part2(testInput)).isEqualTo(-1L);

        assertThat(day.part2(readInput())).isEqualTo(-1L);
    }

}