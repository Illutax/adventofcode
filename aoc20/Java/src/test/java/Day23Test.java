import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


class Day23Test {

    @Test void generateOrdering() {
        // Arrange
        final var buffer = parseInput("583741926");

        // Act
        final var ordering = Day23.generateOrdering(buffer);

        // Assert
        assertThat(ordering, is("92658374"));
    }

    private int[] parseInput(String input) {
        return Arrays.stream(input.split("")).mapToInt(Integer::parseInt)
                        .toArray();
    }

    @Test void doMoves() {
        // Arrange
        final var buffer = parseInput("389125467");

        // Act
        Day23.doMoves(0, 10, buffer);

        // Assert
        assertThat(buffer, is("583741926".toCharArray()));
    }

    @Test void selectDestination_1Step() {
        // Arrange
        final var buffer = parseInput("389125467");
        final int currentCupIndex = 0;

        // Act
        final int destination = Day23.selectDestination(currentCupIndex, buffer);

        // Assert
        assertThat(destination, is(2));
    }

    @Test void selectDestination_2Steps() {
        // Arrange
        final var buffer = parseInput("325467891");
        final int currentCupIndex = 2;

        // Act
        final int destination = Day23.selectDestination(currentCupIndex, buffer);

        // Assert
        assertThat(destination, is(3));
    }

    @Test void selectDestination_underflow_because1isSelected() {
        // Arrange
        final var buffer = parseInput("925841367");
        final int currentCupIndex = 5;

        // Act
        final int destination = Day23.selectDestination(currentCupIndex, buffer);

        // Assert
        assertThat(destination, is(9));
    }

    @Test void shiftSelected_move1() {
        // Arrange
        final var buffer = parseInput("389125467");
        // -- move 1 --
        //cups: (3) 8  9  1  2  5  4  6  7
        //         |-------|  ^
        //pick up: 8, 9, 1
        //destination: 2
        //result: 3 (2) 8  9  1  5  4  6  7
        final int currentCupIndex = 0;
        final int destinationNumber = 2;

        // Act
        Day23.shiftSelected(currentCupIndex, destinationNumber, buffer);

        // Assert
        assertThat(buffer, is(parseInput("328915467")));
    }

    @Test void shiftSelected_move2() {
        // Arrange
        final var buffer = parseInput("328915467");
        //-- move 2 --
        //cups:  3 (2) 8  9  1  5  4  6  7
        //            |-------|           ^
        //pick up: 8, 9, 1
        //destination: 7
        //result: 3  2 (5) 4  6  7  8  9  1
        final int currentCupIndex = 2;
        final int destinationNumber = 3;

        // Act
        Day23.shiftSelected(currentCupIndex, destinationNumber, buffer);

        // Assert
        assertThat(buffer, is(parseInput("325467891")));
    }

    @Test void shiftSelected_move5() {
        // Arrange
        final var buffer = parseInput("325546791");
        //-- move 5 --
        //cups:  3  2  5  8 (4) 6  7  9  1
        //        ^            |-------|
        //pick up: 6, 7, 9
        //destination: 3
        //result: 9  2  5  8  4 (1) 3  6  7
        final int currentCupIndex = 2;
        final int destinationNumber = 3;

        // Act
        Day23.shiftSelected(currentCupIndex, destinationNumber, buffer);

        // Assert
        assertThat(buffer, is(parseInput("925841367")));
    }
}