package tech.dobler.aoc22;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day10Test {

    final String testInput = """
            addx 15
            addx -11
            addx 6
            addx -3
            addx 5
            addx -1
            addx -8
            addx 13
            addx 4
            noop
            addx -1
            addx 5
            addx -1
            addx 5
            addx -1
            addx 5
            addx -1
            addx 5
            addx -1
            addx -35
            addx 1
            addx 24
            addx -19
            addx 1
            addx 16
            addx -11
            noop
            noop
            addx 21
            addx -15
            noop
            noop
            addx -3
            addx 9
            addx 1
            addx -3
            addx 8
            addx 1
            addx 5
            noop
            noop
            noop
            noop
            noop
            addx -36
            noop
            addx 1
            addx 7
            noop
            noop
            noop
            addx 2
            addx 6
            noop
            noop
            noop
            noop
            noop
            addx 1
            noop
            noop
            addx 7
            addx 1
            noop
            addx -13
            addx 13
            addx 7
            noop
            addx 1
            addx -33
            noop
            noop
            noop
            addx 2
            noop
            noop
            noop
            addx 8
            noop
            addx -1
            addx 2
            addx 1
            noop
            addx 17
            addx -9
            addx 1
            addx 1
            addx -3
            addx 11
            noop
            noop
            addx 1
            noop
            addx 1
            noop
            noop
            addx -13
            addx -19
            addx 1
            addx 3
            addx 26
            addx -30
            addx 12
            addx -1
            addx 3
            addx 1
            noop
            noop
            noop
            addx -9
            addx 18
            addx 1
            addx 2
            noop
            noop
            addx 9
            noop
            noop
            noop
            addx -1
            addx 2
            addx -37
            addx 1
            addx 3
            noop
            addx 15
            addx -21
            addx 22
            addx -6
            addx 1
            noop
            addx 2
            addx 1
            noop
            addx -10
            noop
            noop
            addx 20
            addx 1
            addx 2
            addx 2
            addx -6
            addx -11
            noop
            noop
            noop""";
    private Day10 day;

    @BeforeEach
    void setUp() {
        day = new Day10();
    }

    private static Stream<Day10.Instruction> readInput() {
        return Util.readLines(10).map(Day10.Instruction::from);
    }

    @Test
    void part1() {
        assertThat(day.part1(Util.splitByNewLine(testInput, Day10.Instruction::from).limit(11))).isEqualTo(420);
        assertThat(day.part1(Util.splitByNewLine(testInput, Day10.Instruction::from))).isEqualTo(13_140);

        assertThat(day.part1(readInput())).isEqualTo(14_340L);
    }

    @Test
    void part2() {
        //assertThat(day.part2(Util.splitByNewLine(testInput, Day10.Instruction::from).limit(11))).isEqualTo("##..##..##..##..##..#");

        final var expectedOutput = """
                ##..##..##..##..##..##..##..##..##..##..
                ###...###...###...###...###...###...###.
                ####....####....####....####....####....
                #####.....#####.....#####.....#####.....
                ######......######......######......####
                #######.......#######.......#######.....
                """;

        assertThat(day.part2(Util.splitByNewLine(testInput, Day10.Instruction::from))).isEqualTo(expectedOutput);

        final var expectedSolution = """
                ###...##..###....##..##..###..#..#.###..
                #..#.#..#.#..#....#.#..#.#..#.#..#.#..#.
                #..#.#..#.#..#....#.#....###..####.#..#.
                ###..####.###.....#.#....#..#.#..#.###..
                #....#..#.#....#..#.#..#.#..#.#..#.#....
                #....#..#.#.....##...##..###..#..#.#....
                """; // "PAPJCBHP"
        assertThat(day.part2(readInput())).isEqualTo(expectedSolution);
    }
}