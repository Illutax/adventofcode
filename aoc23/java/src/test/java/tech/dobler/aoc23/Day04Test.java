package tech.dobler.aoc23;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.dobler.aoc23.Day04.Card;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day04Test {

    final String testInput = """
            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
            Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
            Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
            Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
            Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
            """;
    private Day04 day;

    @BeforeEach
    void setUp() {
        day = new Day04();
    }

    private static Stream<String> readInput() {
        return Util.readLines(4);
    }

    @Test
    void cardSimpleCtor() {
        assertThat(Day04.Card.of("Card 3: 10 11 | 69 10 63"))
                .isEqualTo(new Card(3, 1));
    }

    @Test
    void cardCtor() {
        assertThat(Day04.Card.of("Card 3:  1  2  4 59 44 | 69 82 63 72 16 21 14  1"))
                .isEqualTo(new Card(3, 1));
    }

    @Test
    void part1() {
        assertThat(day.part1(Util.splitByNewLine(testInput))).isEqualTo(13);

        assertThat(day.part1(readInput())).isEqualTo(17_803);
    }

    @Test
    void part2() {
        assertThat(day.part2(Util.splitByNewLine(testInput))).isEqualTo(30);

        assertThat(day.part2(readInput())).isEqualTo(5_554_894);
    }
}