import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.function.Function;


@Slf4j
public class Day11 {

    public static final int OCCUPIED = 1;
    public static final int GROUND = 2;
    public static final int EMPTY = 0;
    public static final Function<String, Integer> RULES = s -> {
        if (s.equals(".")) return GROUND;
        if (s.equals("#")) return OCCUPIED;
        return EMPTY;
    };

    public static void main(String[] args) {
        var grid = AoCUtil.parseGrid(11, RULES);

        final var result1 = part1(grid);
        assert result1 == 2222 : "nn";
        log.info("Part 1={}", result1);
    }

    @SneakyThrows public static int part1(int[][] grid) {
        grid = copy(grid); // preserve input
        var roundsPassed = 0;
        boolean changed = true;
        while(changed) {
            // loop through grid
            changed = doRound(grid);
            roundsPassed++;
            log.debug("Rounds passed = {}", roundsPassed);

            if (roundsPassed-1 > 2359) throw new InterruptedException("To many runs!");
        };
        return countOccupiedSeats(grid);
    }

    private static int countOccupiedSeats(int[][] grid) {
        int ans = 0;
        for (int[] row : grid) {
            for (int seat : row) {
                if (seat == OCCUPIED)
                    ans++;
            }
        }
        return ans;
    }

    static boolean doRound(int[][] grid) {
        boolean changed = false;
        int[][] oldGrid = copy(grid); // remember old grid
        for(int y = 0; y < grid.length; ++y) {
            for(int x = 0; x < grid[y].length; ++x) {
                if (grid[y][x] == GROUND) continue;

                final int amountOfCccupiedNeighboringSeats = countOccupiedNeighbouringSeats(x, y, oldGrid);
                // had no neighbours
                if (oldGrid[y][x] == EMPTY && amountOfCccupiedNeighboringSeats == 0) {
                    grid[y][x] = OCCUPIED; // becomes occupied
                    log.debug("changed seat at x:{} y:{} to occupied", x, y);
                    changed = true;
                }
                // was occupied and had too many neighbours
                else if (oldGrid[y][x] == OCCUPIED && amountOfCccupiedNeighboringSeats >= 4) {
                    log.debug("changed seat at x:{} y:{} to free", x, y);
                    grid[y][x] = EMPTY; // becomes empty
                    changed = true;
                }
            }
        }
        return changed;
    }

    static boolean doRoundRec(int[][] grid) {
        boolean changed = false;
        int[][] oldGrid = copy(grid); // remember old grid
        for(int y = 0; y < grid.length; ++y) {
            for(int x = 0; x < grid[y].length; ++x) {
                if (grid[y][x] == GROUND) continue;

                final int amountOfCccupiedNeighboringSeats = countOccupiedNeighbouringSeats(x, y, oldGrid);
                // had no neighbours
                if (oldGrid[y][x] == EMPTY && amountOfCccupiedNeighboringSeats == 0) {
                    grid[y][x] = OCCUPIED; // becomes occupied
                    log.debug("changed seat at x:{} y:{} to occupied", x, y);
                    changed = true;
                }
                // was occupied and had too many neighbours
                else if (oldGrid[y][x] == OCCUPIED && amountOfCccupiedNeighboringSeats >= 5) {
                    log.debug("changed seat at x:{} y:{} to free", x, y);
                    grid[y][x] = EMPTY; // becomes empty
                    changed = true;
                }
            }
        }
        return changed;
    }

    private static int[][] copy(int[][] old) {
        return Arrays.stream(old).map(int[]::clone).toArray(int[][]::new);
    }

    static int countOccupiedNeighbouringSeats(int x, int y, int[][] grid) {
        int occupiedSeats = 0;
        for(int i = -1; i < 2; ++i){
            for(int j = -1; j < 2; ++j){
                if (j != 0 || i != 0) occupiedSeats += getOccupiedSeat(grid, x + i, y + j);
            }
        }

        return occupiedSeats;
    }

    private static int getOccupiedSeat(int[][] grid, int x, int y) {
        try {
            if (grid[y][x] == OCCUPIED)
                return OCCUPIED;
        } catch (IndexOutOfBoundsException ex) { /*no-op*/ }
        return EMPTY;
    }
}
