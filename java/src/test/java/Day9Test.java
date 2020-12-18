import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class Day9Test {

    @ParameterizedTest
    @ValueSource(ints = {3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 20, 25, 26, 27, 49})
    void areTwoDifferentAddendsInPreamble_validData_isTrue(int i) {
        // Arrange
        final List<Integer> preamble = IntStream.rangeClosed(1, 25).boxed().collect(Collectors.toList());
        final var day = new Day9();

        // Act
        final boolean result = day.areTwoDifferentAddendsInPreamble(preamble, i);

        // Assert
        assertThat(result, is(true));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 50})
    void areTwoDifferentAddendsInPreamble_invalidData_isFalse(int i) {
        // Arrange
        final List<Integer> preamble = IntStream.rangeClosed(1, 25).boxed().collect(Collectors.toList());
        final var day = new Day9();

        // Act
        final boolean result = day.areTwoDifferentAddendsInPreamble(preamble, i);

        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void smallExample_containsBeforePivot_returnsTrue() {
        // Arrange
        final Collection<Integer> preamble = Arrays.asList(35, 20, 15, 25, 47, 40, 62, 55, 65, 95, 102, 117, 150);
        final var day = new Day9();

        // Act
        final boolean result = day.areTwoDifferentAddendsInPreamble(preamble, 182);

        // Assert
        assertThat(result, is(true));
    }

    @Test
    public void smallExample_containsAtPivot_returnsFalse() {
        // Arrange
        final Collection<Integer> preamble = Arrays.asList(95, 102, 117, 150, 182);
        final var day = new Day9();

        // Act
        final boolean result = day.areTwoDifferentAddendsInPreamble(preamble, 127);

        // Assert
        assertThat(result, is(false));
    }
}