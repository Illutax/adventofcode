package tech.dobler.aoc22;


import tech.dobler.aoc22.Day14.Boundaries;
import tech.dobler.aoc22.Day14.ICoordinate;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static tech.dobler.aoc22.Util.print;


public class Day15 {
    public record Coordinate(int x, int y) implements ICoordinate, Comparable<Coordinate> {
        public static final Pattern PATTERN = Pattern.compile("x=-?\\d+, y=-?\\d+");

        public static Coordinate from(int x, int y) {
            return new Coordinate(x, y);
        }

        public static Coordinate of(String pair) {
            if (!PATTERN.matcher(pair).matches()) {
                throw new IllegalArgumentException("Does not match pattern %s".formatted(pair));
            }

            final var parts = pair.split(",");
            return from(Integer.parseInt(parts[0].substring(2)), Integer.parseInt(parts[1].substring(3)));
        }

        public int distance(Coordinate other) {
            return Math.abs(x - other.x) + Math.abs(y - other.y);
        }

        @Override
        public int compareTo(Coordinate o) {
            return Comparator
                    .comparingInt(Coordinate::y)
                    .thenComparing(Coordinate::x)
                    .compare(this, o);
        }

        public Coordinate add(Coordinate other) {
            return Coordinate.from(x + other.x, y + other.y);
        }
    }

    public record Beacon(Coordinate position) {
    }

    public record Sensor(Coordinate position, Beacon beacon) {
        public static Sensor parse(String line) {
            final var coordinates = Coordinate.PATTERN.matcher(line)
                    .results()
                    .map(MatchResult::group)
                    .map(Coordinate::of)
                    .toList();
            return new Sensor(coordinates.get(0), new Beacon(coordinates.get(1)));
        }
    }

    public enum Cell {
        EMPTY("."), MARKED("#"), SENSOR("S"), BEACON("B");

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

    public record Grid(Map<Coordinate, Cell> map, AtomicReference<Boundaries> boundaries) {
        public static int ROW_OF_INTEREST = 2_000_000;

        public static Grid empty() {
            return new Grid(new HashMap<>(), new AtomicReference<>(Day14.Boundaries.empty()));
        }

        public Grid withRowOfInterest(int rowOfInterest) {
            Grid.ROW_OF_INTEREST = rowOfInterest; // NOSONAR
            return this;
        }

        public void withSensor(Sensor sensor) {
            putInternally(sensor.position, Cell.SENSOR);
            putInternally(sensor.beacon().position, Cell.BEACON);
            placeMarkers(sensor);
        }

        private void placeMarkers(Sensor sensor) {
            var currentPosition = sensor.position();
            final var dist = currentPosition.distance(sensor.beacon().position());
            if (currentPosition.y() - dist - 1 > ROW_OF_INTEREST
                    || currentPosition.y() + dist + 1 < ROW_OF_INTEREST) {
                return;
            }

            var startingBoundry = Math.max(0, dist - currentPosition.y());
            for (int dy = startingBoundry; dy < dist + 1; dy++) {

                // Draw upper pyramid
                if (currentPosition.y() - dy == ROW_OF_INTEREST) {
                    for (int dx = 2 * dy - dist; dx < dist + 1; dx++) {
                        tryPlaceMarker(currentPosition.add(Coordinate.from(dx - dy, -dy)));
                    }
                    break;
                }
            }
            startingBoundry = Math.max(1, dist - currentPosition.y());
            for (int dy = startingBoundry; dy < dist + 1; dy++) {
                // Draw lower pyramid
                if (currentPosition.y() + dy == ROW_OF_INTEREST) {
                    for (int dx = 2 * dy - dist; dx < dist + 1; dx++) {
                        tryPlaceMarker(currentPosition.add(Coordinate.from(dx - dy, dy)));
                    }
                    break;
                }
            }
        }

        private void tryPlaceMarker(Coordinate position) {
            //noinspection SwitchStatementWithTooFewBranches
            switch (map.getOrDefault(position, Cell.EMPTY)) { //NOSONAR
                case EMPTY //
                        -> putInternally(position, Cell.MARKED);
            }
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
                    sb.append(map.getOrDefault(Coordinate.from(b.xMin() + dx, b.yMin() + dy), Cell.EMPTY));
                }
                sb.append("\n");
            }
            return sb.toString();
        }
    }

    public static Stream<Sensor> parseInput(Stream<String> input) {
        return input.map(Sensor::parse);
    }


    public long part1(Stream<Sensor> sensors, int rowOfInterest) {
        final var grid = Grid.empty().withRowOfInterest(rowOfInterest);
        sensors.forEach(grid::withSensor);
        if (rowOfInterest == 10) // actual input is too large for printing
            print(grid.prettyPrint());

        return grid.map().entrySet().stream()
                .filter(it -> it.getKey().y() == Grid.ROW_OF_INTEREST)
                .filter(it -> it.getValue() == Cell.MARKED)
                .count();
    }

    public int part2(Stream<Sensor> sensors) {
        return -1;
    }
}
