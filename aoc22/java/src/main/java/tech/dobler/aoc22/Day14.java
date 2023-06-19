package tech.dobler.aoc22;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;

public class Day14 {
    public enum Cell {
        EMPTY("."), WALL("#"), SOURCE("+"), SAND("o"), SLOPE("~");

        private final String c;

        Cell(String c) {
            this.c = c;
        }

        public boolean isEmpty() {
            return this == EMPTY;
        }

        @Override
        public String toString() {
            return c;
        }
    }

    public interface ICoordinate {
        int x();
        int y();
    }

    public record Coordinate(int x, int y) implements ICoordinate, Comparable<Coordinate> {
        private static final Pattern PATTERN = Pattern.compile("\\d+,\\d+");

        public static Coordinate from(int x, int y) {
            return new Coordinate(x, y);
        }

        public static Coordinate of(String pair) {
            if (!PATTERN.matcher(pair).matches()) {
                throw new IllegalArgumentException("Does not match pattern %s".formatted(pair));
            }

            final var parts = pair.split(",");
            return from(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }

        public List<Coordinate> inBetween(Coordinate other) {
            if (x != other.x && y != other.y)
                throw new IllegalArgumentException("Coordinates have to be either on a horizontal or vertical line %s %s".formatted(this, other));
            final var fromX = Math.min(x, other.x);
            final var toX = Math.max(x, other.x);
            final var fromY = Math.min(y, other.y);
            final var toY = Math.max(y, other.y);

            return fromX == toX
                    ? IntStream.rangeClosed(fromY, toY)
                    .mapToObj(y -> new Coordinate(x, y))
                    .toList()
                    : IntStream.rangeClosed(fromX, toX)
                    .mapToObj(x -> new Coordinate(x, y))
                    .toList();
        }

        public Coordinate down() {
            return from(x, y + 1);
        }

        public Coordinate left() {
            return from(x - 1, y + 1);
        }

        public Coordinate right() {
            return from(x + 1, y + 1);
        }

        @Override
        public int compareTo(Coordinate o) {
            return Comparator
                    .comparingInt(Coordinate::y)
                    .thenComparing(Coordinate::x)
                    .compare(this, o);
        }
    }

    public record Path(List<Coordinate> coordinates) {
        private static final Pattern PATTERN = Pattern.compile("\\d+,\\d+( -> \\d+,\\d+)*");

        public static Path parse(String line) {
            if (!PATTERN.matcher(line).matches()) {
                throw new IllegalArgumentException("Does not match pattern %s".formatted(line));
            }

            final var parts = line.split(" -> ");
            return new Path(Stream.of(parts)
                    .map(Coordinate::of)
                    .toList());
        }
    }

    public record Boundaries(int xMin, int xMax, int yMin, int yMax) {

        public static Boundaries empty() {
            return new Boundaries(MAX_VALUE, MIN_VALUE, MAX_VALUE, MIN_VALUE);
        }

        public Boundaries with(ICoordinate coordinate) {
            if (coordinate.x() < xMin) return new Boundaries(coordinate.x(), xMax, yMin, yMax).with(coordinate);
            if (coordinate.x() > xMax) return new Boundaries(xMin, coordinate.x(), yMin, yMax).with(coordinate);
            if (coordinate.y() < yMin) return new Boundaries(xMin, xMax, coordinate.y(), yMax).with(coordinate);
            if (coordinate.y() > yMax) return new Boundaries(xMin, xMax, yMin, coordinate.y()).with(coordinate);
            return this;
        }
    }

    public enum SimulationState {SPAWNING, FALLING}

    public record Simulation(SimulationState state, Coordinate position, int steps) {
        public static Simulation empty() {
            return new Simulation(SimulationState.SPAWNING, Grid.SOURCE_POSITION, 0);
        }

        public Simulation advance(Coordinate coordinate) {
            return new Simulation(SimulationState.FALLING, coordinate, steps);
        }

        public Simulation next() {
            return new Simulation(switch (state) {
                case FALLING -> SimulationState.SPAWNING;
                case SPAWNING -> SimulationState.FALLING;
            }, Grid.SOURCE_POSITION, steps + 1);
        }
    }

    public record Grid(Map<Coordinate, Cell> map, AtomicReference<Boundaries> boundaries,
                       AtomicReference<Simulation> simulation) {
        public static final Coordinate SOURCE_POSITION = Coordinate.from(500, 0);
        public static final boolean ENABLE_DEBUG_LOGGING = false;

        public Grid() {
            this(new TreeMap<>(),
                    new AtomicReference<>(Boundaries.empty()),
                    new AtomicReference<>(Simulation.empty()));
        }

        public void addPath(Path path) {
            var prev = path.coordinates().get(0);
            for (int i = 1; i < path.coordinates().size(); i++) {
                final var next = path.coordinates().get(i);
                for (Coordinate coordinate : prev.inBetween(next)) {
                    if (ENABLE_DEBUG_LOGGING && map.containsKey(coordinate))
                        Util.printfln("DEBUG: Coordinate %s is already in use", coordinate);
                    putInternally(coordinate, Cell.WALL);
                }
                prev = next;
            }
        }

        public Grid withSource() {
            putInternally(SOURCE_POSITION, Cell.SOURCE);
            return this;
        }

        // For testing
        public boolean simulateOneUnitOfSand() {
            return simulateOneUnitOfSand(12);
        }

        public boolean simulateOneUnitOfSand(int rockBottom) {
            if (map.get(SOURCE_POSITION) == Cell.SAND) return false;
            final var path = new ArrayList<Coordinate>();
            var isFallingEndless = false;
            while (true) {
                final var currentPosition = simulation.get().position;
                final Coordinate down;
                final Coordinate left;
                final Coordinate right;
                if (isEmpty(down = currentPosition.down())) {
                    path.add(down);
                    if (down.y() == rockBottom) {
                        isFallingEndless = true;
                        break;
                    }
                    simulation.getAndUpdate(s -> s.advance(down));
                } else if (isEmpty(left = currentPosition.left())) {
                    path.add(left);
                    simulation.getAndUpdate(s -> s.advance(left));
                } else if (isEmpty(right = currentPosition.right())) {
                    path.add(right);
                    simulation.getAndUpdate(s -> s.advance(right));
                } else {
                    break;
                }
            }
            if (!isFallingEndless) {
                putInternally(simulation.getAndUpdate(Simulation::next).position, Cell.SAND);
                return true;
            }

            path.forEach(p -> putInternally(p, Cell.SLOPE));
            return false;
        }

        private boolean isEmpty(Coordinate coordinate) {
            return map.getOrDefault(coordinate, Cell.EMPTY).isEmpty();
        }

        private void putInternally(Coordinate coordinate, Cell cell) {
            boundaries.getAndUpdate(b -> b.with(coordinate));
            map.put(coordinate, cell);
        }

        public String prettyPrint() {
            StringBuilder sb = new StringBuilder();
            final var b = boundaries().get();
            final var xDiff = b.xMax() - b.xMin();
            final var yDiff = b.yMax() - b.yMin();
            for (int dy = 0; dy <= yDiff; dy++) {
                for (int dx = 0; dx <= xDiff; dx++) {
                    sb.append(map.getOrDefault(Day14.Coordinate.from(b.xMin() + dx, b.yMin() + dy), Day14.Cell.EMPTY));
                }
                sb.append("\n");
            }
            return sb.toString();
        }

        public int getRockBottom() {
            final var last = ((TreeMap<Coordinate, Cell>) map).navigableKeySet().last();
            return last.y;
        }

        public Grid withRockBottom() {
            final int rockBottom = getRockBottom() + 2;
            IntStream.rangeClosed(SOURCE_POSITION.x - rockBottom - 1, SOURCE_POSITION.x + rockBottom + 1)
                    .mapToObj(x -> Coordinate.from(x, rockBottom))
                    .forEach(coordinate -> putInternally(coordinate, Cell.WALL));
            return this;
        }
    }

    public static Stream<Path> parseInput(Stream<String> lines) {
        return lines.map(Path::parse);
    }

    public int part1(Stream<String> lines) {
        final var grid = new Grid()
                .withSource();
        parseInput(lines)
                .forEach(grid::addPath);
        final var rockBottom = grid.getRockBottom() + 3;
        while (grid.simulateOneUnitOfSand(rockBottom)) {
            // Does its thing
        }
        return grid.simulation.get().steps();
    }

    public int part2(Stream<String> lines) {
        final var grid = new Grid()
                .withSource();
        parseInput(lines)
                .forEach(grid::addPath);
        final var rockBottom = grid.getRockBottom() + 2;
        grid.withRockBottom();
        while (grid.simulateOneUnitOfSand(rockBottom)) {
            // Does its thing
        }
        return grid.simulation.get().steps();
    }
}
