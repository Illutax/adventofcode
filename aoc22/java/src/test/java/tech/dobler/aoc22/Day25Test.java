package tech.dobler.aoc22;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tech.dobler.aoc22.Day25.SNAFU;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day25Test {

    final String testInput = """
            1=-0-2
            12111
            2=0=
            21
            2=01
            111
            20012
            112
            1=-1=
            1-12
            12
            1=
            122""";
    private Day25 day;

    @BeforeEach
    void setUp() {
        day = new Day25();
    }

    private static Stream<SNAFU> readInput() {
        return Util.readLines(25).map(SNAFU::new);
    }

    @Nested
    class Snafu {

        @Test
        void _1() {
            final var result = 1;
            final var input = "1";
            assertThat(new SNAFU(input).decoded()).isEqualTo(result);
            assertThat(SNAFU.of(result).encoded()).isEqualTo(input);
        }

        @Test
        void _2() {
            final var result = 2;
            final var input = "2";
            assertThat(new SNAFU(input).decoded()).isEqualTo(result);
            assertThat(SNAFU.of(result).encoded()).isEqualTo(input);
        }

        @Test
        void _3() {
            final var result = 3;
            final var input = "1=";
            assertThat(new SNAFU(input).decoded()).isEqualTo(result);
            assertThat(SNAFU.of(result).encoded()).isEqualTo(input);
        }

        @Test
        void _4() {
            final var result = 4;
            final var input = "1-";
            assertThat(new SNAFU(input).decoded()).isEqualTo(result);
            assertThat(SNAFU.of(result).encoded()).isEqualTo(input);
        }

        @Test
        void _5() {
            final var result = 5;
            final var input = "10";
            assertThat(new SNAFU(input).decoded()).isEqualTo(result);
            assertThat(SNAFU.of(result).encoded()).isEqualTo(input);
        }

        @Test
        void _6() {
            final var result = 6;
            final var input = "11";
            assertThat(new SNAFU(input).decoded()).isEqualTo(result);
            assertThat(SNAFU.of(result).encoded()).isEqualTo(input);
        }

        @Test
        void _7() {
            final var result = 7;
            final var input = "12";
            assertThat(new SNAFU(input).decoded()).isEqualTo(result);
            assertThat(SNAFU.of(result).encoded()).isEqualTo(input);
        }

        @Test
        void _8() {
            final var result = 8;
            final var input = "2=";
            assertThat(new SNAFU(input).decoded()).isEqualTo(result);
            assertThat(SNAFU.of(result).encoded()).isEqualTo(input);
        }

        @Test
        void _9() {
            final var result = 9;
            final var input = "2-";
            assertThat(new SNAFU(input).decoded()).isEqualTo(result);
            assertThat(SNAFU.of(result).encoded()).isEqualTo(input);
        }

        @Test
        void _10() {
            final var result = 10;
            final var input = "20";
            assertThat(new SNAFU(input).decoded()).isEqualTo(result);
            assertThat(SNAFU.of(result).encoded()).isEqualTo(input);
        }

        @Test
        void _15() {
            final var result = 15;
            final var input = "1=0";
            assertThat(new SNAFU(input).decoded()).isEqualTo(result);
            assertThat(SNAFU.of(result).encoded()).isEqualTo(input);
        }

        @Test
        void _20() {
            final var result = 20;
            final var input = "1-0";
            assertThat(new SNAFU(input).decoded()).isEqualTo(result);
            assertThat(SNAFU.of(result).encoded()).isEqualTo(input);
        }

        @Test
        void _2022() {
            final var result = 2022;
            final var input = "1=11-2";
            assertThat(new SNAFU(input).decoded()).isEqualTo(result);
            assertThat(SNAFU.of(result).encoded()).isEqualTo(input);
        }

        @Test
        void _12345() {
            final var result = 12345;
            final var input = "1-0---0";
            assertThat(new SNAFU(input).decoded()).isEqualTo(result);
            assertThat(SNAFU.of(result).encoded()).isEqualTo(input);
        }

        @Test
        void _314159265() {
            final var result = 314159265;
            final var input = "1121-1110-1=0";
            assertThat(new SNAFU(input).decoded()).isEqualTo(result);
            assertThat(SNAFU.of(result).encoded()).isEqualTo(input);
        }

        @Test
        void part1() {
            assertThat(day.part1(Util.splitByNewLine(testInput).map(SNAFU::new))).isEqualTo(-1);

            assertThat(day.part1(readInput())).isEqualTo(-1);
        }

        @Disabled
        @Test
        void part2() {
            assertThat(day.part2(Util.splitByNewLine(testInput).map(SNAFU::new))).isEqualTo(-1);

            assertThat(day.part2(readInput())).isEqualTo(-1);
        }
    }
}
