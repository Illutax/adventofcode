package tech.dobler.aoc24;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;


@Slf4j
public class Day06 {

    public enum Cell {
        EMPTY,
        OBSTACEL,
        START;

        Cell() {
        }

        public static Cell from(String cell) {
            return switch (cell) {
                case "." -> Cell.EMPTY;
                case "#" -> Cell.OBSTACEL;
                case "^" -> Cell.START;
                default -> throw new IllegalStateException("Unexpected value: " + cell);
            };
        }
    }

    @EqualsAndHashCode
    @ToString
    public static class Grid {
        private final Map<String, Vec2> cardinalDirections = Map.of(
                "^", Vec2.of(0, -1), //NORTH
                ">", Vec2.of(1, 0), //EAST
                "v", Vec2.of(0, 1), //SOUTH
                "<", Vec2.of(-1, 0) //WEST
        );
        private final Vec2 bounds;
        private final Cell[][] cells;
        private Vec2 startPosition;
        @Getter
        private boolean[][] visitedCells;

        @Setter
        @Getter
        private Vec2 newObstacle;
        @Getter
        private Vec2 guardPosition;
        private Vec2 guardOrientation = this.cardinalDirections.get("^");

        public Grid(Cell[][] cells) {
            this.cells = cells;
            for (var y = 0; y < cells.length; y++) {
                var row = cells[y];
                for (var x = 0; x < row.length; x++) {
                    if (row[x] == Cell.START) {
                        this.guardPosition = Vec2.of(x, y);
                        this.startPosition = Vec2.of(x, y);
                        break;
                    }
                }
            }
            this.bounds = Vec2.of(cells[0].length, cells.length);
        }

        public static Grid parse(String input) {
            final Cell[][] cells = Arrays.stream(input.split("\n"))
                    .map(line -> Arrays.stream(line.split("")).map(Cell::from).toArray(Cell[]::new))
                    .toArray(Cell[][]::new);
            return new Grid(cells);
        }

        public long amountOfTraversedUniqueCells() {
            return Stream.of(this.visitedCells).flatMap(row -> IntStream.range(0, row.length)
                    .mapToObj(idx -> row[idx]).filter(it -> it)).count();
        }

        public void traverse() {
            this.visitedCells = new boolean[this.bounds.y()][this.bounds.x()];
            while (true) {
                while (this.getCellAtGuardsPosition().isPresent() &&
                        this.getCellAtGuardsPosition().map(cell -> !cell.equals(Cell.OBSTACEL)).orElse(false)) { // path is clear
                    this.visit(this.guardPosition);
                    this.guardPosition = this.guardPosition.plus(this.guardOrientation);
                }
                if (this.getCellAtGuardsPosition().isEmpty()) return; // inside grid, REALLY? //FIXME
                this.guardPosition = this.guardPosition.plus(this.guardOrientation.mult(-1));
                this.guardOrientation = this.guardOrientation.rotateCW();
            }
        }

        public Set<PosAndDir> loops() {
            Assert.notNull(this.newObstacle, "newObstacle");
            this.guardPosition = this.startPosition;
            this.guardOrientation = this.cardinalDirections.get("^");
            final var visited = new HashSet<PosAndDir>();
            while (true) {
                while (
                        this.getCellAtGuardsPosition().isPresent() && // inside grid
                                this.getCellAtGuardsPosition().map(cell -> !cell.equals(Cell.OBSTACEL)).orElse(false) &&
                                !this.guardPosition.equals(this.newObstacle)) {
                    if (visited.contains(PosAndDir.of(this.guardPosition, this.guardOrientation))) {
                        return visited;
                    }
                    visited.add(PosAndDir.of(this.guardPosition, this.guardOrientation));
                    this.guardPosition = this.guardPosition.plus(this.guardOrientation);
                }
                if (this.getCellAtGuardsPosition().isEmpty()) return Set.of(); // inside grid
                this.guardPosition = this.guardPosition.plus(this.guardOrientation.mult(-1));
                this.guardOrientation = this.guardOrientation.rotateCW();
            }
        }

        public List<Vec2> possibleLocationsForNewObstacle() {
            final var result = new ArrayList<Vec2>();
            for (int y = 0; y < this.visitedCells.length; y++) {
                for (int x = 0; x < this.visitedCells[y].length; x++) {
                    if (this.visitedCells[y][x]) {
                        result.add(Vec2.of(x, y));
                    }
                }
            }
            return result;
        }

        private void visit(Vec2 pos) {
            this.visitedCells[pos.y()][pos.x()] = true;
        }

        private Optional<Cell> getCellAtGuardsPosition() {
            final var x = this.guardPosition.x();
            final var y = this.guardPosition.y();
            if (x < 0 || y < 0 || x >= this.bounds.x() || y >= this.bounds.y())
                return Optional.empty();

            return Optional.ofNullable(this.cells[y][x]);
        }
    }

    public record PosAndDir(Vec2 pos, Vec2 dir) {
        private static final Map<String, PosAndDir> cache = new HashMap<>();

        public static PosAndDir of(Vec2 pos, Vec2 dir) {
            final var key = "%d,%d|%d,%d".formatted(pos.x(), pos.y(), dir.x(), dir.y());
            return cache.computeIfAbsent(key, _ -> new PosAndDir(pos, dir));
        }
    }

    record Configuration(Set<PosAndDir> posAndDirs) {
        public int size() {
            return this.posAndDirs.size();
        }
    }

    public long part1(String input) {
        final var grid = Grid.parse(input);
        grid.traverse();
        return grid.amountOfTraversedUniqueCells();
    }

    public long part2(String input) {
        final var grid = Grid.parse(input);
        grid.traverse();
        var possibleLocationsForNewObstacle = grid.possibleLocationsForNewObstacle();
        log.info("Checking {} possibleLocationsForNewObstacle", possibleLocationsForNewObstacle.size());
        var allExistingConfigurations = new HashSet<Configuration>();
        possibleLocationsForNewObstacle.forEach(newObstacle -> {
            grid.newObstacle = newObstacle;
            var newConfiguration = grid.loops();
            if (!newConfiguration.isEmpty()) {
                allExistingConfigurations.add(new Configuration(newConfiguration));
                log.info("Accepting: {}", newConfiguration.size());
            }
        });

        return allExistingConfigurations.size();
    }
}