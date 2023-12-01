package tech.dobler.aoc22;


import tech.dobler.aoc22.Day14.ICoordinate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;


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

    public record Sensor(Coordinate position, Beacon beacon, int distance) {
        public static Sensor of(Coordinate... coordinates) {
            if (coordinates.length != 2)
                throw new IllegalArgumentException("Sensor requires 2 coordinates! But got %s".formatted(Arrays.toString(coordinates)));
            return new Sensor(coordinates[0], new Beacon(coordinates[1]), coordinates[0].distance(coordinates[1]));
        }

        public static Sensor parse(String line) {
            final var coordinates = Coordinate.PATTERN.matcher(line)
                    .results()
                    .map(MatchResult::group)
                    .map(Coordinate::of)
                    .toArray(Coordinate[]::new);
            return Sensor.of(coordinates);
        }

        public boolean distanceCheck(Coordinate pos) {
            final var distance = pos.distance(position());
            return distance <= this.distance();
        }
    }

    public static List<Sensor> parseInput(Stream<String> input) {
        return input.map(Sensor::parse).toList();
    }

    public long part1(List<Sensor> sensors, int rowOfInterest) {
       return -1;
    }

    public long part2(List<Sensor> sensors, int upperBound) {
       return -1;
    }


}
