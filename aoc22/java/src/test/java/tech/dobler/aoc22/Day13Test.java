package tech.dobler.aoc22;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tech.dobler.aoc22.Day13.Multiple;
import tech.dobler.aoc22.Day13.PacketPair;
import tech.dobler.aoc22.Day13.Single;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class Day13Test {

    final String testInput = """
            [1,1,3,1,1]
            [1,1,5,1,1]
                        
            [[1],[2,3,4]]
            [[1],4]
                        
            [9]
            [[8,7,6]]
                        
            [[4,4],4,4]
            [[4,4],4,4,4]
                        
            [7,7,7,7]
            [7,7,7]
                        
            []
            [3]
                        
            [[[]]]
            [[]]
                        
            [1,[2,[3,[4,[5,6,7]]]],8,9]
            [1,[2,[3,[4,[5,6,0]]]],8,9]
            """;
    private Day13 day;

    @BeforeEach
    void setUp() {
        day = new Day13();
    }

    private static String readInput() {
        return Util.readInput(13);
    }

    @Nested
    class MultipleTests {

        @Test
        void entryList_shallowCtor() {
            final var input = "[1,1,3,1,1]";

            final var entryList = Multiple.parseLine(input);

            assertThat(entryList)
                    .isEqualTo(new Multiple(List.of(
                            new Single(1),
                            new Single(1),
                            new Single(3),
                            new Single(1),
                            new Single(1)
                    )));
        }

        @Test
        void entryList_simpleNestingCtor() {
            final var input = "[[1],4]";

            final var entryList = Multiple.parseLine(input);

            assertThat(entryList)
                    .isEqualTo(new Multiple(List.of(
                            new Multiple(List.of(new Single(1))),
                            new Single(4)
                    )));
        }

        @Test
        void entryList_twoNestingsCtor() {
            final var input = "[[1],[2,3,4]]";

            final var entryList = Multiple.parseLine(input);

            assertThat(entryList)
                    .isEqualTo(new Multiple(List.of(
                            new Multiple(List.of(new Single(1))),
                            new Multiple(List.of(new Single(2), new Single(3), new Single(4)))
                    )));
        }

        @Test
        void entryList_deeplyNestedCtor() {
            final var input = "[1,[2,[3]],4]";

            final var entryList = Multiple.parseLine(input);

            assertThat(entryList)
                    .isEqualTo(new Multiple(List.of(
                            new Single(1),
                            new Multiple(List.of(new Single(2),
                                    new Multiple(List.of(new Single(3))))),
                            new Single(4)
                    )));
        }
    }

    @Nested
    class Sanity {
        @Test
        void ctorPacketPair() {
            assertThatNullPointerException()
                    .isThrownBy(() -> new PacketPair(null, null));
        }
    }

    @Test
    void part1() {
        assertThat(day.part1(Day13.parseAsPairs(testInput))).isEqualTo(13);

        assertThat(day.part1(Day13.parseAsPairs(readInput())))
                .isLessThan(7028)
                .isLessThan(6267)
                .isEqualTo(5013);
    }

    @Test
    void part2() {

        assertThat(day.part2(Day13.parseToList(testInput))).isEqualTo(140);

        assertThat(day.part2(Day13.parseToList(readInput()))).isEqualTo(25038);
    }
}
