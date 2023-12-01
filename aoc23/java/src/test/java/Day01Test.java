import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day01Test {

    final String testInput = """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet
            """;

    final String testInput2 = """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen
            """;
    private Day01 day;

    @BeforeEach
    void setUp() {
        day = new Day01();
    }

    private static Stream<String> readInput() {
        return Util.readLines(1);
    }

    @Test
    void part1() {
        assertThat(day.part1(Util.splitByNewLine(testInput))).isEqualTo(142);

        assertThat(day.part1(readInput())).isEqualTo(55_108);
    }

    @Test
    void part2() {
        assertThat(day.part2(Util.splitByNewLine(testInput2))).isEqualTo(281);

        assertThat(day.part2(readInput()))
                .isGreaterThan(56_322)
                .isLessThan(56_377)
                .isEqualTo(56_324);
    }

    @Test
    void overlap1() {
        assertThat(day.preprocess("eighthree")).isEqualTo("83e");
    }

    @Test
    void overlap2() {
        assertThat(day.preprocess("sevenine")).isEqualTo("79e");
    }

    @Disabled
    @Test
    void preprocess3() {
        assertThat(day.preprocess("abcone2threexyz")).isEqualTo("abc123xyz");
    }

    @Disabled
    @Test
    void preprocessReddit1() {
        assertThat(day.preprocess("eighthree")).isEqualTo("83");
    }

    @Disabled
    @Test
    void preprocess5() {
        assertThat(day.preprocess("4nineeightseven2")).isEqualTo("49872");
    }
}