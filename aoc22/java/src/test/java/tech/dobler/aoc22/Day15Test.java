package tech.dobler.aoc22;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tech.dobler.aoc22.Day15.Beacon;
import tech.dobler.aoc22.Day15.Coordinate;
import tech.dobler.aoc22.Day15.Sensor;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.dobler.aoc22.Day15.parseInput;

class Day15Test {

    final String testInput = """
            Sensor at x=2, y=18: closest beacon is at x=-2, y=15
            Sensor at x=9, y=16: closest beacon is at x=10, y=16
            Sensor at x=13, y=2: closest beacon is at x=15, y=3
            Sensor at x=12, y=14: closest beacon is at x=10, y=16
            Sensor at x=10, y=20: closest beacon is at x=10, y=16
            Sensor at x=14, y=17: closest beacon is at x=10, y=16
            Sensor at x=8, y=7: closest beacon is at x=2, y=10
            Sensor at x=2, y=0: closest beacon is at x=2, y=10
            Sensor at x=0, y=11: closest beacon is at x=2, y=10
            Sensor at x=20, y=14: closest beacon is at x=25, y=17
            Sensor at x=17, y=20: closest beacon is at x=21, y=22
            Sensor at x=16, y=7: closest beacon is at x=15, y=3
            Sensor at x=14, y=3: closest beacon is at x=15, y=3
            Sensor at x=20, y=1: closest beacon is at x=15, y=3
            """;
    private Day15 day;

    @BeforeEach
    void setUp() {
        day = new Day15();
    }

    private static Stream<String> readInput() {
        return Util.readLines(15);
    }

    @Nested
    class Primitives {
        @Test
        void parseSensor() {
            final var sensor = Sensor.parse("Sensor at x=2, y=18: closest beacon is at x=-2, y=15");

            assertThat(sensor).isEqualTo(
                    new Sensor(
                            Coordinate.from(2,18), new
                            Beacon(Coordinate.from(-2,15))));
        }
    }


    @Test
    void part1() {
        assertThat(day.part1(parseInput(Util.splitByNewLine(testInput)))).isEqualTo(26);

        assertThat(day.part1(parseInput(readInput()))).isEqualTo(-1);
    }

    @Disabled
    @Test
    void part2() {
        assertThat(day.part2(parseInput(Util.splitByNewLine(testInput)))).isEqualTo(-1);

        assertThat(day.part2(parseInput(readInput()))).isEqualTo(-1);
    }
}
