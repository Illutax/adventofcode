import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


class Day24Test {

    @Test void part1() {
        // Arrange
        var lines = AoCUtil.parseInputToStringList(24);

        // Act
        final long amountOfFlippedTiles = Day24.part1(lines);

        // Assert
        assertThat(amountOfFlippedTiles, is(266L));
    }

    @Test void part1_sample() {
        // Arrange
        var lines = AoCUtil.parseInputToStringList(24_0);

        // Act
        final long amountOfFlippedTiles = Day24.part1(lines);

        // Assert
        assertThat(amountOfFlippedTiles, is(10L));
    }

    @Test void part2_sample() {
        // Arrange
        var lines = AoCUtil.parseInputToStringList(24_0);

        // Act
        final long amountOfFlippedTiles = Day24.part2(lines);

        // Assert
        assertThat(amountOfFlippedTiles, is(2208L));
    }

    @Test void getDirections_esew() {
        // Arrange
        final var expectedDirections = List
                        .of(Direction.EAST, Direction.SOUTH_EAST,
                                        Direction.WEST);

        // Act
        final var result = Day24.getDirections("esew");

        // Assert
        assertThat(result, is(expectedDirections));
    }

    @Test void getDirections_nwwswee() {
        // Arrange
        final var expectedDirections = List
                        .of(Direction.NORTH_WEST, Direction.WEST,
                                        Direction.SOUTH_WEST, Direction.EAST,
                                        Direction.EAST);

        // Act
        final var result = Day24.getDirections("nwwswee");

        // Assert
        assertThat(result, is(expectedDirections));
    }

    @Test void walkDirections_esew() {
        // Arrange
        final var directionsToWalk = Day24.getDirections("esew");

        // Act
        final var result = Day24.walkDirections(directionsToWalk);

        // Assert
        assertThat(result, is(new Point(0, 1)));
    }

    @Test void walkDirections_nwwswee() {
        // Arrange
        final var directionsToWalk = Day24.getDirections("nwwswee");

        // Act
        final var result = Day24.walkDirections(directionsToWalk);

        // Assert
        assertThat(result, is(new Point(0, 0)));
    }

    @Test void countAdjestBlackTiles() {
        // Arrange
        final Map<Point, Boolean> tiles = Map.of(
                        Direction.EAST.getDirection(), Boolean.TRUE,
                        Direction.WEST.getDirection(), Boolean.TRUE,
                        Direction.NORTH_EAST.getDirection(), Boolean.TRUE,
                        Direction.NORTH_WEST.getDirection(), Boolean.TRUE,
                        Direction.SOUTH_EAST.getDirection(), Boolean.TRUE,
                        Direction.SOUTH_WEST.getDirection(), Boolean.TRUE
                        );

        // Act
        final var result = Day24.countAdjestBlackTiles(tiles, new Point(0, 0));

        // Assert
        assertThat(result, is(6L));
    }

    @Test void permuteExhibition_OneBlackEntry() {
        // Arrange
        final Map<Point, Boolean> initialTiles = new HashMap<>();
        final Point pointOfInterest = new Point(0, 0);
        initialTiles.put(pointOfInterest, Boolean.TRUE);

        // Act
        Day24.permuteExhibition(initialTiles);

        // Assert
        assertThat(initialTiles.size(), is(7));
        assertThat(initialTiles.get(pointOfInterest), is(false));
    }

    @Test void permuteExhibition_OneWhiteEntry_StaysUnflipped() {
        // Arrange
        final Map<Point, Boolean> initialTiles = new HashMap<>();
        initialTiles.put(new Point(0, 0), Boolean.FALSE);
        final Map<Point, Boolean> expectedTiles = new HashMap<>();
        expectedTiles.put(new Point(0, 0), Boolean.FALSE);

        // Act
        Day24.permuteExhibition(initialTiles);

        // Assert
        assertThat(initialTiles.size(), is(1));
        assertThat(initialTiles, is(expectedTiles));
    }

    @Test void permuteExhibition_OneWhiteEntryWithTwoBlackNeighbours() {
        // Arrange
        final Map<Point, Boolean> initialTiles = new HashMap<>(){{
            put(new Point(0, 0), Boolean.FALSE);
            put(new Point(1, 0), Boolean.TRUE);
            put(new Point(-1, 0), Boolean.TRUE);
        }};
        final Map<Point, Boolean> expectedTiles = new HashMap<>(){{
            put(new Point(0, 0), Boolean.TRUE);
            put(new Point(1, 0), Boolean.FALSE);
            put(new Point(-1, 0), Boolean.FALSE);
        }};

        // Act
        Day24.permuteExhibition(initialTiles);

        // Assert
        assertThat(initialTiles.size(), is(13));
        for( var point : expectedTiles.keySet())
            assertThat(initialTiles.get(point), is(expectedTiles.get(point)));
    }
}