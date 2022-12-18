package tech.dobler.aoc22;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day07Test {

    final String testInput = """
            $ cd /
            $ ls
            dir a
            14848514 b.txt
            8504156 c.dat
            dir d
            $ cd a
            $ ls
            dir e
            29116 f
            2557 g
            62596 h.lst
            $ cd e
            $ ls
            584 i
            $ cd ..
            $ cd ..
            $ cd d
            $ ls
            4060174 j
            8033020 d.log
            5626152 d.ext
            7214296 k
            """;
    private Day07 day;

    @BeforeEach
    void setUp() {
        day = new Day07();
    }

    private static List<String> readInput() {
        return Util.splitToList(7);
    }

    @Test
    void part1() {
        assertThat(day.part1(Util.splitToList(testInput))).isEqualTo(95_437);

        assertThat(day.part1(readInput()))
                .isGreaterThan(911_275)
                .isGreaterThan(932_203)
                .isGreaterThan(976_068)
                .isEqualTo(1_084_134);
    }

    @Test
    void part2() {
        assertThat(day.part2(Util.splitToList(testInput))).isEqualTo(24_933_642);

        assertThat(day.part2(readInput()))
                .isLessThan(25_622_272L)
                .isEqualTo(6_183_184);
    }
}