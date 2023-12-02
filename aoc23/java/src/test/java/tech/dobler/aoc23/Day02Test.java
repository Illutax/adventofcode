package tech.dobler.aoc23;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.dobler.aoc23.Util.readLines;
import static tech.dobler.aoc23.Util.splitByNewLine;

class Day02Test {

    final String testInput = """
            Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
            Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
            Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
            Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
            """;
    private Day02 day;

    @BeforeEach
    void setUp() {
        day = new Day02();
    }

    private static Stream<String> readInput() {
        return readLines(2);
    }

    @Test
    void BagCtor() {
        final var expectedBag = new Day02.Bag(6,5,11);
        Day02.Bag bag = Day02.Bag.of("5 green, 11 blue, 6 red");
        assertThat(bag).isEqualTo(expectedBag);
    }

    @Test
    void part1() {
        assertThat(day.part1(splitByNewLine(testInput))).isEqualTo(8);

        assertThat(day.part1(readInput()))
                .isLessThan(3_075)
                .isEqualTo(2_683);
    }

    @Test
    void part2() {
        assertThat(day.part2(splitByNewLine(testInput))).isEqualTo(2286);

        assertThat(day.part2(readInput())).isEqualTo(49_710);
    }
}