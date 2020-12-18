import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.Is.is;

class Day9Test {

    public static final long THE_ANSWER = 42;

    @ParameterizedTest
    @ValueSource(ints = {3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 20, 25, 26, 27, 49})
    void areTwoDifferentAddendsInPreamble_validData_isTrue(int i) {
        // Arrange
        final List<Integer> preamble = IntStream.rangeClosed(1, 25).boxed().collect(Collectors.toList());

        // Act
        final boolean result = Day9.areTwoDifferentAddendsInPreamble(preamble, i);

        // Assert
        assertThat(result, is(true));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 50})
    void areTwoDifferentAddendsInPreamble_invalidData_isFalse(int i) {
        // Arrange
        final List<Integer> preamble = IntStream.rangeClosed(1, 25).boxed().collect(Collectors.toList());

        // Act
        final boolean result = Day9.areTwoDifferentAddendsInPreamble(preamble, i);

        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void smallExample_containsBeforePivot_returnsTrue() {
        // Arrange
        final Collection<Integer> preamble = Arrays.asList(35, 20, 15, 25, 47, 40, 62, 55, 65, 95, 102, 117, 150);

        // Act
        final boolean result = Day9.areTwoDifferentAddendsInPreamble(preamble, 182);

        // Assert
        assertThat(result, is(true));
    }

    @Test
    public void smallExample_containsAtPivot_returnsFalse() {
        // Arrange
        final Collection<Integer> preamble = Arrays.asList(95, 102, 117, 150, 182);

        // Act
        final boolean result = Day9.areTwoDifferentAddendsInPreamble(preamble, 127);

        // Assert
        assertThat(result, is(false));
    }


    /* Part 2 */
    @Test
    public void consecutiveSum_oneNumber_returnsTheNumber() {
        // Arrange
        final var number = 7L;
        Collection<Long> preamble = Collections.singleton(number);

        // Act
        var result = Day9.consecutiveSum(preamble, 0, 0);

        // Assert
        assertThat(result, is(number));
    }

    @Test
    public void consecutiveSum_twoNumbers_returnsTheirSum() {
        // Arrange
        Collection<Long> preamble = Arrays.asList(7L, THE_ANSWER);

        // Act
        var result = Day9.consecutiveSum(preamble, 0, 1);

        // Assert
        assertThat(result, is(7 + THE_ANSWER));
    }

    public void consecutiveSum_threeNumbers_returnsTheirSum() {
        // Arrange
        Collection<Long> preamble = Arrays.asList(7L, THE_ANSWER, THE_ANSWER);

        // Act
        var result = Day9.consecutiveSum(preamble, 0, 2);

        // Assert
        assertThat(result, is(7 + THE_ANSWER * 2));
    }

    public void consecutiveSum_threeNumbers_returnsTheSumOfFirstTwo() {
        // Arrange
        Collection<Long> preamble = Arrays.asList(7L, THE_ANSWER, THE_ANSWER);

        // Act
        var result = Day9.consecutiveSum(preamble, 0, 1);

        // Assert
        assertThat(result, is(7 + THE_ANSWER));
    }

    @Test
    public void consecutiveSum_beforeSubSequence_returnsFalse() {
        // Arrange
        final Collection<Long> preamble = Arrays.asList(20L, 15L, 25L, 47L);

        // Act
        final var result = Day9.consecutiveSum(preamble, 0, 3);

        // Assert
        assertThat(result, is(107L));
    }


    @Test
    public void hasSequence_fullSequence_returnsTrue() {
        // Arrange
        final Collection<Long> preamble = Arrays.asList(35L, 20L, 15L, 25L, 47L, 40L, 62L, 55L, 65L, 95L, 102L, 117L, 150L);

        // Act
        final boolean result = Day9.hasPreambleSummedSubsequence(preamble, 127);

        // Assert
        assertThat(result, is(true));
    }

    @Test
    public void hasSequence_beforeSubSequence_returnsFalse() {
        // Arrange
        final Collection<Long> preamble = Arrays.asList(20L, 15L, 25L, 47L);

        // Act
        final boolean result = Day9.hasPreambleSummedSubsequence(preamble, 127);

        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void hasSequence_afterSubSequence_returnsFalse() {
        // Arrange
        final Collection<Long> preamble = Arrays.asList(25L, 47L, 40L, 62L);

        // Act
        final boolean result = Day9.hasPreambleSummedSubsequence(preamble, 127);

        // Assert
        assertThat(result, is(false));
    }

    @Test
    public void hasSequence_exactlyAtSubsequence_returnsTrue() {
        // Arrange
        final Collection<Long> preamble = Arrays.asList(15L, 25L, 47L, 40L);

        // Act
        final boolean result = Day9.hasPreambleSummedSubsequence(preamble, 127);

        // Assert
        assertThat(result, is(true));
    }

    @Test
    public void hasSequence_moreThanSubSequence_returnsTrue() {
        // Arrange
        final Collection<Long> preamble = Arrays.asList(20L, 15L, 25L, 47L, 40L, 62L);

        // Act
        final boolean result = Day9.hasPreambleSummedSubsequence(preamble, 127);

        // Assert
        assertThat(result, is(true));
    }
}