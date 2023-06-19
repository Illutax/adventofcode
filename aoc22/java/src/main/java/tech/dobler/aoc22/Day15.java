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
import static tech.dobler.aoc22.Util.printfln;


public class Day15 {
    public record Coordinate(int x, int y) implements ICoordinate, Comparable<Coordinate> {
        public static final Pattern PATTERN = Pattern.compile("x=-?\\d+, y=-?\\d+");
        public static final Coordinate DIAGONALLY_TO_THE_RIGHT = Coordinate.from(1, -1);

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
            return Math.max(x, other.x) - Math.min(x, other.x)
                    + Math.max(y, other.y) - Math.min(y, other.y);
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
        public static Grid empty() {
            return new Grid(new HashMap<>(), new AtomicReference<>(Day14.Boundaries.empty()));
        }

        public void withSensor(Sensor sensor) {
            putInternally(sensor.position, Cell.SENSOR);
            putInternally(sensor.beacon().position, Cell.BEACON);
            placeMarkers(sensor);
        }

        private void placeMarkers(Sensor sensor) {
            final var dist = sensor.position.distance(sensor.beacon.position);
            printfln("Filling sensor with distance: %d, area: %d", dist, dist*dist*2L);
            var currentPosition = sensor.position;
            // go to x-dist
            currentPosition = currentPosition.add(Coordinate.from(-dist - 1, 1));
            for (int col = 0; col < dist; col++) {
                // add (1,1) dist times
                for (int row = 0; row <= dist; row++) {
                    currentPosition = currentPosition.add(Coordinate.DIAGONALLY_TO_THE_RIGHT);
                    tryPlaceMarker(currentPosition);
                }
                // go (-dist-1,-dist-2) and repeat dist times
                currentPosition = currentPosition.add(Coordinate.from(-dist, dist + 1));

                for (int row = 0; row < dist; row++) {
                    currentPosition = currentPosition.add(Coordinate.DIAGONALLY_TO_THE_RIGHT);
                    tryPlaceMarker(currentPosition);
                }
                currentPosition = currentPosition.add(Coordinate.from(-dist, dist + 1));
            }
            for (int row = 0; row <= dist; row++) {
                currentPosition = currentPosition.add(Coordinate.DIAGONALLY_TO_THE_RIGHT);
                tryPlaceMarker(currentPosition);
            }
        }

        private void tryPlaceMarker(Coordinate position) {
            //noinspection SwitchStatementWithTooFewBranches
            switch (map.getOrDefault(position, Cell.EMPTY)) { //NOSONAR
                case EMPTY -> putInternally(position, Cell.MARKED);
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


    public int part1(Stream<Sensor> sensors) {
        final var grid = Grid.empty();
        sensors.forEach(grid::withSensor);
        print(grid.prettyPrint());

        return (int) grid.map().entrySet().stream()
                .filter(it -> it.getKey().y() == 10)
                .filter(it -> it.getValue() == Cell.MARKED)
                .count();
    }

    public int part2(Stream<Sensor> sensors) {
        return -1;
    }
}
