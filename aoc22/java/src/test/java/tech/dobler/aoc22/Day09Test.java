package tech.dobler.aoc22;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Day09Test {

    final String testInput = """
            R 4
            U 4
            L 3
            D 1
            R 4
            D 1
            L 5
            R 2""";

    final String testInput2 = """
            R 5
            U 8
            L 8
            D 3
            R 17
            D 10
            L 25
            U 20""";
    private Day09 day;

    @BeforeEach
    void setUp() {
        day = new Day09();
    }

    private static Stream<Day09.Instruction> readInput() {
        return Util.readLines(9).map(Day09.Instruction::from);
    }

    @Test
    void Direction_from_simple() {
        assertThat(Day09.Direction.from(0,0)).isEmpty();
        assertThat(Day09.Direction.from(1,0)).isEmpty();
        assertThat(Day09.Direction.from(0,1)).isEmpty();
        assertThat(Day09.Direction.from(-1,0)).isEmpty();
        assertThat(Day09.Direction.from(0,-1)).isEmpty();
        assertThat(Day09.Direction.from(2,0)).containsExactlyInAnyOrder(Day09.Direction.R);
        assertThat(Day09.Direction.from(0,2)).containsExactlyInAnyOrder(Day09.Direction.D);
        assertThat(Day09.Direction.from(-2,0)).containsExactlyInAnyOrder(Day09.Direction.L);
        assertThat(Day09.Direction.from(0,-2)).containsExactlyInAnyOrder(Day09.Direction.U);
    }

    @Test
    void Direction_from_complex() {
        assertThat(Day09.Direction.from(1,1)).isEmpty();
        assertThat(Day09.Direction.from(-1,-1)).isEmpty();
        assertThat(Day09.Direction.from(2, -1)).containsExactlyInAnyOrder(Day09.Direction.R, Day09.Direction.U);
    }

    @Test
    void Position_squareDistance() {
        assertThat(new Day09.Position(2,2).squareDistance(new Day09.Position(3,2))).isEqualTo(1);
        assertThat(new Day09.Position(3,2).squareDistance(new Day09.Position(2,2))).isEqualTo(1);
        assertThat(new Day09.Position(2,2).squareDistance(new Day09.Position(2,3))).isEqualTo(1);
        assertThat(new Day09.Position(2,2).squareDistance(new Day09.Position(3,3))).isEqualTo(2);
        assertThat(new Day09.Position(2,2).squareDistance(new Day09.Position(4,3))).isEqualTo(5);
        assertThat(new Day09.Position(2,2).squareDistance(new Day09.Position(4,2))).isEqualTo(4);
    }

    @Nested
    class Rope {

        private Day09.Rope rope;

        @BeforeEach
        void setUp() {
            rope = new Day09.Rope(2);
        }

        @Test
        void moveLeftTwice() {
            rope.move(Day09.Instruction.from("L 2"));
            assertThat(rope.getHead()).isEqualTo(new Day09.Position(-2,0));
            assertThat(rope.getTail()).isEqualTo(new Day09.Position(-1,0));
        }

        @Test
        void moveRightUpRight() {
            rope.move(Day09.Instruction.from("R 1"));
            assertThat(rope.getTail()).isEqualTo(Day09.Position.START);

            rope.move(Day09.Instruction.from("U 1"));
            assertThat(rope.getTail()).isEqualTo(Day09.Position.START);
            assertThat(rope.getHead()).isEqualTo(new Day09.Position(1,-1));

            rope.move(Day09.Instruction.from("R 1"));
            assertThat(rope.getHead()).isEqualTo(new Day09.Position(2,-1));
            assertThat(rope.getTail()).isEqualTo(new Day09.Position(1,-1));
        }
    }

    @Test
    void part1() {
        assertThat(day.part1(Util.splitByNewLine(testInput, Day09.Instruction::from))).isEqualTo(13);
        assertThat(day.part1(Util.splitByNewLine(testInput2, Day09.Instruction::from))).isEqualTo(88);

        assertThat(day.part1(readInput())).isEqualTo(6_197);
    }

    @Test
    void part2() {
        assertThat(day.part2(Util.splitByNewLine(testInput,Day09.Instruction::from))).isEqualTo(1);
        assertThat(day.part2(Util.splitByNewLine(testInput2,Day09.Instruction::from))).isEqualTo(36);

        assertThat(day.part2(readInput())).isEqualTo(2_562);
    }
}