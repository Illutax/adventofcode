import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


class Day10Test {

    @Test public void part1() {
        // Arrange
        var adapters = Arrays.asList(3, 1, 10, 7, 6);

        // Act
        final int result = Day10.part1(adapters);

        // Assert
        assertThat(result, is(6));
    }

    @Test public void part1_small() {
        // Arrange
        var adapters = Arrays.asList(16, 10, 15, 5, 1, 11, 7, 19, 6, 12, 4);

        // Act
        final int result = Day10.part1(adapters);

        // Assert
        assertThat(result, is(35));
    }

    @Test public void part1_big() {
        // Arrange
        var adapters = Arrays.asList(28, 33, 18, 42, 31, 14, 46, 20, 48, 47,
                        24, 23, 49, 45, 19, 38, 39, 11, 1, 32, 25, 35, 8, 17, 7, 9, 4, 2, 34, 10, 3);

        // Act
        final int result = Day10.part1(adapters);

        // Assert
        assertThat(result, is(220));
    }


    @Test public void recursiveSolve() {
        // Arrange
        var adapters = Arrays.asList(1, 3, 6, 7, 10);

        // Act
        final var result = Day10.part2(adapters);

        // Assert
        assertThat(result, is(2L));
    }

    @Test public void recursiveSolve2() {
        // Arrange
        var adapters = Arrays.asList(1, 2, 3, 4);

        // Act
        final var result = Day10.part2(adapters);

        // Assert
        assertThat(result, is(7L));
    }

    @Test public void recursiveSolveNeg() {
        // Arrange
        var adapters = Arrays.asList(1, 6, 7, 10);

        // Act
        final var result = Day10.part2(adapters);

        // Assert
        assertThat(result, is(0L));
    }

    @Test public void recursiveSolve_small() {
        // Arrange
        var adapters = Arrays.asList(16, 10, 15, 5, 1, 11, 7, 19, 6, 12, 4);

        // Act
        final var result = Day10.part2(adapters);

        // Assert
        assertThat(result, is(8L));
    }

    @Test public void recursiveSolve_big() {
        // Arrange
        var adapters = Arrays.asList(28, 33, 18, 42, 31, 14, 46, 20, 48, 47,
                        24, 23, 49, 45, 19, 38, 39, 11, 1, 32, 25, 35, 8, 17, 7, 9, 4, 2, 34, 10, 3);

        // Act
        final var result = Day10.part2(adapters);

        // Assert
        assertThat(result, is(19_208L));
    }

}