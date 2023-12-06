package tech.dobler.aoc23;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tech.dobler.aoc23.Day05.Interval;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day05Test {

    final String testInput = """
            seeds: 79 14 55 13
                        
            seed-to-soil map:
            50 98 2
            52 50 48
                        
            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15
                        
            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4
                        
            water-to-light map:
            88 18 7
            18 25 70
                        
            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13
                        
            temperature-to-humidity map:
            0 69 1
            1 0 69
                        
            humidity-to-location map:
            60 56 37
            56 93 4
            """;
    private Day05 day;

    @BeforeEach
    void setUp() {
        day = new Day05();
    }

    private static List<String> readInput() {
        return Util.splitToList(5);
    }

    @Test
    void part1() {
        assertThat(day.part1(Util.splitToList(testInput))).isEqualTo(35);

        assertThat(day.part1(readInput())).isEqualTo(579_439_039L);
    }

    @Test
    void part2() {
        assertThat(day.part2(Util.splitToList(testInput))).isEqualTo(46);

        assertThat(day.part2(readInput()))
                .isLessThan(652_851_272L)
                .isEqualTo(7_873_084L);
    }

    @Nested
    class IntervalTests {
        @Test
        void exactlyFull() {
            Interval initial = new Interval(5, 7);
            var split = initial.split(new Interval(5, 7));
            //noinspection AssertBetweenInconvertibleTypes
            assertThat(split).containsExactly(initial)
                    .element(0)
                    .isSameAs(initial);
        }

        @Test
        void full() {
            var split = new Interval(5, 7).split(new Interval(4, 8));
            assertThat(split).containsExactly(new Interval(5, 7));
        }

        @Test
        void fullInside() {
            var split = new Interval(1, 8).split(new Interval(3, 5));
            assertThat(split).containsExactly(new Interval(3, 5), new Interval(1, 3), new Interval(5, 8));
        }

        @Test
        void splitLeft() {
            var split = new Interval(5, 7).split(new Interval(3, 6));
            assertThat(split).containsExactly(new Interval(5, 6), new Interval(6, 7));
        }

        @Test
        void splitRight() {
            var split = new Interval(5, 7).split(new Interval(6, 9));
            assertThat(split).containsExactly(new Interval(6, 7), new Interval(5, 6));
        }
    }
}