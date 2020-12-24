import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


class Day11Test {

    int[][] testGrid = AoCUtil.parseGrid(11_0, Day11.RULES);

    @Test public void part1_TestInput() {
        // Arrange
        testGrid = AoCUtil.parseGrid(11_0, Day11.RULES);

        // Act
        final int occ = Day11.part1(testGrid);

        // Assert
        assertThat(occ, is(37));
    }

    @Test public void part2_TestInput() {
        // Arrange
        testGrid = AoCUtil.parseGrid(11_0, Day11.RULES);

        // Act
        final int occ = Day11.part2(testGrid);

        // Assert
        assertThat(occ, is(26));
    }

    @Test public void doRound() {
        // Arrange
        int[][] afterOneRound = new int[][] {
                        new int []{1,2,1,1,2,1,1,2,1,1},
                        new int []{1,1,1,1,1,1,1,2,1,1},
                        new int []{1,2,1,2,1,2,2,1,2,2},
                        new int []{1,1,1,1,2,1,1,2,1,1},
                        new int []{1,2,1,1,2,1,1,2,1,1},
                        new int []{1,2,1,1,1,1,1,2,1,1},
                        new int []{2,2,1,2,1,2,2,2,2,2},
                        new int []{1,1,1,1,1,1,1,1,1,1},
                        new int []{1,2,1,1,1,1,1,1,2,1},
                        new int []{1,2,1,1,1,1,1,2,1,1},
        };

        int[][] afterTwoRounds = new int[][] {
                        new int[] {1,2,0,0,2,0,1,2,1,1},
                        new int[] {1,0,0,0,0,0,0,2,0,1},
                        new int[] {0,2,0,2,0,2,2,0,2,2},
                        new int[] {1,0,0,0,2,0,0,2,0,1},
                        new int[] {1,2,0,0,2,0,0,2,0,0},
                        new int[] {1,2,0,0,0,0,1,2,1,1},
                        new int[] {2,2,0,2,0,2,2,2,2,2},
                        new int[] {1,0,0,0,0,0,0,0,0,1},
                        new int[] {1,2,0,0,0,0,0,0,2,0},
                        new int[] {1,2,1,0,0,0,0,2,1,1},
        };

        testGrid = AoCUtil.parseGrid(11_0, Day11.RULES);

        // Act
        boolean changed = Day11.doRound(testGrid);

        // Assert
        assertThat(changed, is(true));
        assertThat(testGrid, is(afterOneRound));

        // Act
        changed = Day11.doRound(testGrid);

        // Assert
        assertThat(changed, is(true));
        assertThat(testGrid, is(afterTwoRounds));
    }

    @Test public void doRound_4times() {
        // Arrange
        testGrid = AoCUtil.parseGrid(11_0, Day11.RULES);

        // Act
        boolean changed = true;
        for(int i = 0; i < 6; ++i)
            changed = Day11.doRound(testGrid);

        // Assert
        assertThat(changed, is(false));
    }

    @Test
    public void countOccupiedNeighbouringSeats() {
        // Arrange
        int[][] grid = new int[][] {
                        new int[] {0, 0 ,0},
                        new int[] {0, 0 ,0},
                        new int[] {0, 0 ,0}
        };

        // Act
        final int occupiedNeighbouringSeats = Day11.countOccupiedNeighbouringSeats(0, 0, grid);

        // Assert
        assertThat(occupiedNeighbouringSeats, is(0));
    }

    @Test
    public void countOccupiedNeighbouringSeats_3() {
        // Arrange
        int[][] grid = new int[][] {
                        new int[] {1,1,1},
                        new int[] {1,1,1},
                        new int[] {1,1,1},
        };

        // Act
        final int occupiedNeighbouringSeats = Day11.countOccupiedNeighbouringSeats(0, 0, grid);

        // Assert
        assertThat(occupiedNeighbouringSeats, is(3));
    }

    @Test
    public void countOccupiedNeighbouringSeats_8() {
        // Arrange
        int[][] grid = new int[][] {
                        new int[] {1,1,1},
                        new int[] {1,1,1},
                        new int[] {1,1,1},
        };

        // Act
        final int occupiedNeighbouringSeats = Day11.countOccupiedNeighbouringSeats(1, 1, grid);

        // Assert
        assertThat(occupiedNeighbouringSeats, is(8));
    }

    @Test
    public void countOccupiedNeighbouringSeatsRec_2() {
        // Arrange
        int[][] grid = new int[][] {
                        new int[] {2,2,2,2,2,2,2,1,2},
                        new int[] {2,2,2,1,2,2,2,2,2},
                        new int[] {2,1,2,2,2,2,2,2,2},
                        new int[] {2,2,2,2,2,2,2,2,2},
                        new int[] {2,2,1,0,2,2,2,2,1},
                        new int[] {2,2,2,2,1,2,2,2,2},
                        new int[] {2,2,2,2,2,2,2,2,2},
                        new int[] {1,2,2,2,2,2,2,2,2},
                        new int[] {2,2,2,1,2,2,2,2,2},
        };

        // Act
        final int occupiedNeighbouringSeats = Day11.countOccupiedNeighbouringSeatsRec(0, 0, grid);

        // Assert
        assertThat(occupiedNeighbouringSeats, is(2));
    }

    @Test
    public void countOccupiedNeighbouringSeatsRec() {
        // Arrange
        int[][] grid = new int[][] {
                        new int[] {2,2,2,2,2,2,2,1,2},
                        new int[] {2,2,2,1,2,2,2,2,2},
                        new int[] {2,1,2,2,2,2,2,2,2},
                        new int[] {2,2,2,2,2,2,2,2,2},
                        new int[] {2,2,1,0,2,2,2,2,1},
                        new int[] {2,2,2,2,1,2,2,2,2},
                        new int[] {2,2,2,2,2,2,2,2,2},
                        new int[] {1,2,2,2,2,2,2,2,2},
                        new int[] {2,2,2,1,2,2,2,2,2},
        };

        // Act
        final int occupiedNeighbouringSeats = Day11.countOccupiedNeighbouringSeatsRec(3, 4, grid);

        // Assert
        assertThat(occupiedNeighbouringSeats, is(8));
    }


}