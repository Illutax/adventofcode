import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


class Day24Test {

    @Test void part1() {
        // Arrange
        var lines = AoCUtil.parseInputToListStrings(24);

        // Act
        final long amountOfFlippedTiles = Day24.part1(lines);

        // Assert
        assertThat(amountOfFlippedTiles, is(266L));
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
        assertThat(result, is(Point.ORIGIN));
    }
}