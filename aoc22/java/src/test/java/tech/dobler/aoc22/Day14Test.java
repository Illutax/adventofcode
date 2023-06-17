package tech.dobler.aoc22;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tech.dobler.aoc22.Day14.Boundaries;
import tech.dobler.aoc22.Day14.Coordinate;
import tech.dobler.aoc22.Day14.Grid;
import tech.dobler.aoc22.Day14.Path;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tech.dobler.aoc22.Day14.Cell.WALL;
import static tech.dobler.aoc22.Day14.parseInput;

class Day14Test {

    final String testInput = """
            498,4 -> 498,6 -> 496,6
            503,4 -> 502,4 -> 502,9 -> 494,9
            """;
    private Day14 day;

    @BeforeEach
    void setUp() {
        day = new Day14();
    }

    private static Stream<String> readInput() {
        return Util.readLines(14);
    }

    @Nested
    class Sanity {
        @Test
        void format() {
            final var matcher = Pattern.compile("\\d+,\\d+").matcher(readInput().collect(Collectors.joining()));
            final var list = matcher.results()
                    .map(MatchResult::group)
                    .sorted(Comparator.reverseOrder())
                    .toList();
            assertTrue(true);
        }
    }

    @Nested
    class Primitives {
        @Test
        void parseCoordinate() {
            assertThat(Coordinate.of("498,4")).isEqualTo(new Coordinate(498, 4));
            assertThat(Coordinate.of("521,150")).isEqualTo(new Coordinate(521, 150));
        }

        @Test
        void parsePath() {
            assertThat(Path.parse("498,4 -> 498,6"))
                    .isEqualTo(new Path(List.of(
                            new Coordinate(498, 4),
                            new Coordinate(498, 6)
                    )));
        }

        @Test
        void inBetween() {
            final var c1 = Coordinate.of("498,4");
            final var c2 = Coordinate.of("498,5");
            final var c3 = Coordinate.of("498,6");

            final var actual = c1.inBetween(c3);
            assertThat(actual).isEqualTo(List.of(c1, c2, c3));
        }

        @Test
        void inBetweenBackwards() {
            final var c1 = Coordinate.of("498,4");
            final var c3 = Coordinate.of("498,6");

            final var actual = c3.inBetween(c1);
            assertThat(actual).isEqualTo(c1.inBetween(c3));
        }

        @Test
        void boundariesCtor() {
            final var boundaries = Boundaries.empty();
            assertThat(boundaries.xMin()).isEqualTo(Integer.MAX_VALUE);
            assertThat(boundaries.xMax()).isEqualTo(Integer.MIN_VALUE);
            assertThat(boundaries.yMin()).isEqualTo(Integer.MAX_VALUE);
            assertThat(boundaries.yMax()).isEqualTo(Integer.MIN_VALUE);
        }

        @Test
        void boundariesAddingTwo() {
            final var boundaries = Boundaries.empty()
                    .with(Coordinate.of("498,4"))
                    .with(Coordinate.of("496,8"));

            assertThat(boundaries.xMin()).isEqualTo(496);
            assertThat(boundaries.xMax()).isEqualTo(498);
            assertThat(boundaries.yMin()).isEqualTo(4);
            assertThat(boundaries.yMax()).isEqualTo(8);
        }
    }

    @Nested
    class GridTests {
        @Test
        void twoStraightPaths() {
            final var grid = new Grid();
            grid.addPath(Path.parse("498,4 -> 498,6"));
            grid.addPath(Path.parse("497,5 -> 499,5"));
            assertThat(grid.map())
                    .isEqualTo(Map.of(
                            new Coordinate(498, 4), WALL,
                            new Coordinate(498, 5), WALL,
                            new Coordinate(498, 6), WALL,
                            new Coordinate(497, 5), WALL,
                            new Coordinate(499, 5), WALL
                    ));
        }

        @Test
        void withHookedPath() {
            final var grid = new Grid();
            final var path = Path.parse("498,4 -> 498,6 -> 496,6");
            grid.addPath(path);
            assertThat(grid.map())
                    .isEqualTo(Map.of(
                            new Coordinate(498, 4), WALL,
                            new Coordinate(498, 5), WALL,
                            new Coordinate(498, 6), WALL,
                            new Coordinate(497, 6), WALL,
                            new Coordinate(496, 6), WALL
                    ));
        }

        @Test
        void toStringTest() {
            final var grid = getTestInputSetup();

            assertThat(grid.prettyPrint()).isEqualTo("""
                    ......+...
                    ..........
                    ..........
                    ..........
                    ....#...##
                    ....#...#.
                    ..###...#.
                    ........#.
                    ........#.
                    #########.
                    """);
        }

        @Test
        void sandSimulation() {
            final var grid = getTestInputSetup();
            grid.simulateOneUnitOfSand();

            assertThat(grid.prettyPrint()).isEqualTo("""
                    ......+...
                    ..........
                    ..........
                    ..........
                    ....#...##
                    ....#...#.
                    ..###...#.
                    ........#.
                    ......o.#.
                    #########.
                    """);
            grid.simulateOneUnitOfSand();

            assertThat(grid.prettyPrint()).isEqualTo("""
                    ......+...
                    ..........
                    ..........
                    ..........
                    ....#...##
                    ....#...#.
                    ..###...#.
                    ........#.
                    .....oo.#.
                    #########.
                    """);
            grid.simulateOneUnitOfSand();

            assertThat(grid.prettyPrint()).isEqualTo("""
                    ......+...
                    ..........
                    ..........
                    ..........
                    ....#...##
                    ....#...#.
                    ..###...#.
                    ........#.
                    .....ooo#.
                    #########.
                    """);
            grid.simulateOneUnitOfSand();

            assertThat(grid.prettyPrint()).isEqualTo("""
                    ......+...
                    ..........
                    ..........
                    ..........
                    ....#...##
                    ....#...#.
                    ..###...#.
                    ......o.#.
                    .....ooo#.
                    #########.
                    """);
            for (int i = 3; i <= 22; i++) {
                grid.simulateOneUnitOfSand();
            }
            assertThat(grid.prettyPrint()).isEqualTo("""
                    ......+...
                    ..........
                    ......o...
                    .....ooo..
                    ....#ooo##
                    ...o#ooo#.
                    ..###ooo#.
                    ....oooo#.
                    .o.ooooo#.
                    #########.
                    """);
            // DONE

            grid.simulateOneUnitOfSand();

            assertThat(grid.prettyPrint()).isEqualTo("""
                    .......+...
                    .......~...
                    ......~o...
                    .....~ooo..
                    ....~#ooo##
                    ...~o#ooo#.
                    ..~###ooo#.
                    ..~..oooo#.
                    .~o.ooooo#.
                    ~#########.
                    ~..........
                    ~..........
                    ~..........
                    """);
            // Sanity check
            assertThat(grid.simulation().get().steps()).isEqualTo(24);
        }

        @Test
        void gridWithRockBottom() {
            final var grid = getTestInputSetup()
                    .withRockBottom();

            assertThat(grid.prettyPrint()).isEqualTo("""
                    ............+............
                    .........................
                    .........................
                    .........................
                    ..........#...##.........
                    ..........#...#..........
                    ........###...#..........
                    ..............#..........
                    ..............#..........
                    ......#########..........
                    .........................
                    #########################
                    """);
        }
    }

    private Grid getTestInputSetup() {
        final var grid = new Grid()
                .withSource();
        parseInput(Util.splitByNewLine(testInput))
                        .forEach(grid::addPath);
        return grid;
    }

    @Test
    void part1() {
        assertThat(day.part1(Util.splitByNewLine(testInput))).isEqualTo(24);

        assertThat(day.part1(readInput())).isEqualTo(768);
    }

    @Test
    void part2() {
        assertThat(day.part2(Util.splitByNewLine(testInput))).isEqualTo(93);

        assertThat(day.part2(readInput()))
                .isLessThan(27_556)
                .isLessThan(27_599)
                .isEqualTo(26_686);
    }
}
