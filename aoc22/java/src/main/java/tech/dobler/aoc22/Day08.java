package tech.dobler.aoc22;

import java.awt.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day08 {

    class Grid {
        private final int width;
        private final int height;
        private int[][] cells;

        public Grid(Stream<String> rows) {
            cells = rows.map(row -> row.chars()
                            .map(i -> i - 48)
                            .toArray())
                    .toArray(int[][]::new);

            width = cells[0].length;
            height = cells.length;
        }

        public int visibleOnTheEdge() {
            return cells[0].length * 2 + (cells.length - 2) * 2;
        }

        public int visibleInTheInterior() {
            int result = 0;
            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {
                    final var visible = visibleFromTop(x, y)
                            || visibleFromBottom(x, y)
                            || visibleFromLeft(x, y)
                            || visibleFromRight(x, y);
                    result += (visible ? 1 : 0);
                }
            }
            return result;
        }

        private boolean visibleFromTop(int x, int y) {
            final var treeHeight = cells[y][x];
            for (int dy = y - 1; dy >= 0; dy--) {
                if (cells[dy][x] >= treeHeight) return false;
            }
            return true;
        }

        private boolean visibleFromBottom(int x, int y) {
            final var treeHeight = cells[y][x];
            for (int dy = y + 1; dy < height; dy++) {
                if (cells[dy][x] >= treeHeight) return false;
            }
            return true;
        }

        private boolean visibleFromLeft(int x, int y) {
            final var treeHeight = cells[y][x];
            for (int dx = x - 1; dx >= 0; dx--) {
                if (cells[y][dx] >= treeHeight) return false;
            }
            return true;
        }

        private boolean visibleFromRight(int x, int y) {
            final var treeHeight = cells[y][x];
            for (int dx = x + 1; dx < width; dx++) {
                if (cells[y][dx] >= treeHeight) return false;
            }
            return true;
        }

        public int highestScenicView() {
            return IntStream.range(1, height - 1)
                    .boxed()
                    .flatMapToInt(y -> IntStream.range(1, width - 1)
                            .map(x -> scenicView(x, y)))
                    .max().orElseThrow();
        }

        private int scenicView(int x, int y) {
            return visibilityFromTop(x, y)
                    * visibilityFromBottom(x, y)
                    * visibilityFromLeft(x, y)
                    * visibilityFromRight(x, y);
        }

        private int visibilityFromTop(int x, int y) {
            final var treeHeight = cells[y][x];
            for (int dy = y - 1; dy >= 0; dy--) {
                if (cells[dy][x] >= treeHeight) return y - dy;
            }
            return y;
        }

        private int visibilityFromBottom(int x, int y) {
            final var treeHeight = cells[y][x];
            for (int dy = y + 1; dy < height; dy++) {
                if (cells[dy][x] >= treeHeight) return dy - y;
            }
            return height - y - 1;
        }

        private int visibilityFromLeft(int x, int y) {
            final var treeHeight = cells[y][x];
            for (int dx = x - 1; dx >= 0; dx--) {
                if (cells[y][dx] >= treeHeight) return x - dx;
            }
            return x;
        }

        private int visibilityFromRight(int x, int y) {
            final var treeHeight = cells[y][x];
            for (int dx = x + 1; dx < width; dx++) {
                if (cells[y][dx] >= treeHeight) return dx - x;
            }
            return width - x - 1;
        }
    }

    public int part1(Stream<String> rows) {
        var grid = new Grid(rows);
        return grid.visibleOnTheEdge() + grid.visibleInTheInterior();
    }

    public int part2(Stream<String> rows) {
        var grid = new Grid(rows);
        return grid.highestScenicView();
    }
}