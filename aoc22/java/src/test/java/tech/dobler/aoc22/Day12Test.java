package tech.dobler.aoc22;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.dobler.aoc22.Day12.canConnect;
import static tech.dobler.aoc22.Day12.transform;

class Day12Test {

    final String testInput = """
            Sabqponm
            abcryxxl
            accszExk
            acctuvwj
            abdefghi""";
    private Day12 day;

    @BeforeEach
    void setUp() {
        day = new Day12();
    }

    private static Day12.Node readInput() {
        return transform(Util.readInput(12));
    }

    @Nested
    class CanConnect {

        @Nested
        class Reversed {
            @Test
            void endToZ() {
                assertThat(Day12.canConnectReverse('E', 'z')).isTrue();
            }

            @Test
            void endToY() {
                assertThat(Day12.canConnectReverse('E', 'y')).isTrue();
            }

            @Test
            void endToX() {
                assertThat(Day12.canConnectReverse('E', 'x')).isFalse();
            }

            @Test
            void endToW() {
                assertThat(Day12.canConnectReverse('E', 'w')).isFalse();
            }

            @Test
            void zToX() {
                assertThat(Day12.canConnectReverse('z', 'x')).isFalse();
            }
        }

        @Nested
        class Forward {

            @Test
            void startWithA() {
                assertThat(canConnect('S', 'a')).isTrue();
            }

            @Test
            void aWithStart() {
                assertThat(canConnect('a', 'S')).isTrue();
            }

            @Test
            void startWithB() {
                assertThat(canConnect('S', 'b')).isTrue();
            }

            @Test
            void startWithC() {
                assertThat(canConnect('S', 'c')).isFalse();
            }

            @Test
            void xWithEnd() {
                assertThat(canConnect('x', 'E')).isFalse();
            }

            @Test
            void yWithEnd() {
                assertThat(canConnect('y', 'E')).isTrue();
            }

            @Test
            void zWithEnd() {
                assertThat(canConnect('z', 'E')).isTrue();
            }
        }
    }

    @Test
    void part1() {
        assertThat(day.part1(transform(testInput))).isEqualTo(31);

        assertThat(day.part1(readInput())).isEqualTo(462);
    }

    @Test
    void part2() {
        assertThat(day.part2(transform(testInput))).isEqualTo(29);

        assertThat(day.part2(readInput())).isEqualTo(451);
    }
}