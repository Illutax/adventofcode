package tech.dobler.aoc22;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day06Test {

    record TestData(String msg, int start1, int start2) {
        public static TestData of(String msg, int start1, int start2) {
            return new TestData(msg, start1, start2);
        }
    }

    final TestData testInput1 = TestData.of("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 7, 19);
    final TestData testInput2 = TestData.of("bvwbjplbgvbhsrlpgdmjqwftvncz", 5, 23);
    final TestData testInput3 = TestData.of("nppdvjthqldpwncqszvftbrmjlhg", 6, 23);
    final TestData testInput4 = TestData.of("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 10, 29);
    final TestData testInput5 = TestData.of("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 11, 26);

    private Day06 day;

    @BeforeEach
    void setUp() {
        day = new Day06();
    }

    private static String readInput()
    {
        return Util.readInput(6);
    }

    @Test
    void part1() {
        assertThat(day.part1(testInput1.msg)).isEqualTo(testInput1.start1);
        assertThat(day.part1(testInput2.msg)).isEqualTo(testInput2.start1);
        assertThat(day.part1(testInput3.msg)).isEqualTo(testInput3.start1);
        assertThat(day.part1(testInput4.msg)).isEqualTo(testInput4.start1);
        assertThat(day.part1(testInput5.msg)).isEqualTo(testInput5.start1);

        final var contents = readInput();
        assertThat(day.part1(contents)).isEqualTo(1953);
    }

    @Test
    void part2() {
        assertThat(day.part2(testInput1.msg)).isEqualTo(testInput1.start2);
        assertThat(day.part2(testInput2.msg)).isEqualTo(testInput2.start2);
        assertThat(day.part2(testInput3.msg)).isEqualTo(testInput3.start2);
        assertThat(day.part2(testInput4.msg)).isEqualTo(testInput4.start2);
        assertThat(day.part2(testInput5.msg)).isEqualTo(testInput5.start2);

        assertThat(day.part2(readInput())).isEqualTo(2301);
    }
}