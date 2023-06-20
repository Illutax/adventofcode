package tech.dobler.aoc22;


import tech.dobler.aoc22.Day14.Boundaries;
import tech.dobler.aoc22.Day14.ICoordinate;

import java.lang.Math;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static tech.dobler.aoc22.Util.*;


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

    public record SemiEagerGrid(Map<Coordinate, Cell> map, AtomicReference<Boundaries> boundaries, int rowOfInterest) {

        public static SemiEagerGrid empty(int rowOfInterest) {
            return new SemiEagerGrid(new HashMap<>(), new AtomicReference<>(Day14.Boundaries.empty()), rowOfInterest);
        }

        public void withSensor(Sensor sensor) {
            putInternally(sensor.position, Cell.SENSOR);
            putInternally(sensor.beacon().position, Cell.BEACON);
            placeMarkers(sensor);
        }

        private void placeMarkers(Sensor sensor) {
            var currentPosition = sensor.position();
            final var dist = sensor.distance();
            if (currentPosition.y() - dist - 1 > rowOfInterest
                    || currentPosition.y() + dist + 1 < rowOfInterest) {
                return;
            }

            var startingBoundry = 0;//Math.max(0, dist - currentPosition.y());
            for (int dy = startingBoundry; dy < dist + 1; dy++) {

                // Draw upper pyramid
                if (currentPosition.y() - dy == rowOfInterest) {
                    for (int dx = 2 * dy - dist; dx < dist + 1; dx++) {
                        tryPlaceMarker(currentPosition.add(Coordinate.from(dx - dy, -dy)));
                    }
                    break;
                }
            }
            startingBoundry = 1;//Math.max(1, dist - currentPosition.y());
            for (int dy = startingBoundry; dy < dist + 1; dy++) {
                // Draw lower pyramid
                if (currentPosition.y() + dy == rowOfInterest) {
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

    public static List<Sensor> parseInput(Stream<String> input) {
        return input.map(Sensor::parse).toList();
    }

    public long part1(List<Sensor> sensors, int rowOfInterest) {
        final var grid = SemiEagerGrid.empty(rowOfInterest);
        sensors.forEach(grid::withSensor);
        if (false && rowOfInterest == 10) // puzzle input is too large for printing
            print(grid.prettyPrint());

        return grid.map().entrySet().stream()
                .filter(it -> it.getKey().y() == rowOfInterest)
                .filter(it -> it.getValue() == Cell.MARKED)
                .count();
    }

    // On my current hardware it would take approximately 11-30 days to finish computing.
    // Well, or not: Parsing 0,042 percent took 1187,650 s (approx till end: 785,483197 h)
    //    Exception in thread "main" java.lang.OutOfMemoryError
    public long part2_LOL(List<Sensor> sensors, int upperBound) {

        // In Part 1 we started with creating an grid eagerly.
        // When the execution (and rendering) of the actual input didn't finished in 2 min,
        // we restricted the evaluation to only the row on interest.
        //
        // In Part 2 we need to fill not an row of interest but a square and then find one space where an beacon can be.
        // For that we need to evaluate the cells by iterating over all sensors and determine whether it lies in its circumference.
        // We should also consider using an lookup map for the distance, because we will
        final var map = sensors.stream()
                .map(Sensor::position)
                .collect(Collectors.toMap(it -> it, __ -> Cell.SENSOR));
        sensors.stream()
                .map(s -> s.beacon().position())
                .distinct()
                .collect(Collectors.toMap(it -> it, __ -> Cell.BEACON))
                .forEach((k, v) -> map.merge(k, v, (v1, v2) -> v2));
        final var maxIterations = (1 + upperBound) * (long) (1 + upperBound);
        printfln("Iterating %d times", maxIterations);
        return positionInListToFrequency(upperBound + 1, crunchTheNumbers(upperBound, maxIterations, map, sensors) - 1);
    }

    private static long crunchTheNumbers(int upperBound, long maxIterations, Map<Coordinate, Cell> map, List<Sensor> sensors) {
        final var counter = new AtomicLong();
        final var timer = System.currentTimeMillis();
        return makeSquareCoordinateStreamOfBoundingArea(upperBound)
                .map(pos -> {
                    /*synchronized (counter)*/
                    {
                        if (counter.incrementAndGet() % 1_600_000_000 == 0) {
                            final double fraction = ((double) counter.get()) / maxIterations;
                            final long deltaTime = System.currentTimeMillis() - timer;
                            printf("Parsing %.3f percent", fraction * 100);
                            printfln(" took %.3f s (approx till end: %f h)", deltaTime / 1000.0, (deltaTime / fraction / 1000.0 / 3600));
                        }
                    }
                    return map.getOrDefault(pos,
                            sensors.stream()
                                    .filter(s -> distanceCheck(pos, s))
                                    .findFirst()
                                    .map(__ -> Cell.MARKED)
                                    .orElse(Cell.EMPTY)
                    );
                })
                .map(it -> it != Cell.EMPTY)
                .takeWhile(it -> it)
                .count();
    }

    private static long positionInListToFrequency(int upperBound, long position) {
        final var y = position / upperBound;
        final var x = position - y * upperBound;
        return (1 + x) * 4000000L + y;
    }

    private static boolean distanceCheck(Coordinate pos, Sensor s) {
        final var distance = pos.distance(s.position());
        return distance <= s.distance();
    }

    public long part2(List<Sensor> sensors, int upperBound) {
        int y = 0;
        int x = 0;
        int i = 0;
        final var results = crunchNumbersInThreads(sensors, upperBound);
        for (; i < upperBound; i++) {
            final var result = results[i];
            if (-1 + result != upperBound) {
                x = result;
                printfln("->%d", result);
                break;
            }
            y++;
        }
        return x * 4000000L + y;
    }

    private int[] crunchNumbersInThreads(List<Sensor> sensors, int upperBound) {
        final var results = new int[upperBound + 1];
        final AtomicBoolean stop = new AtomicBoolean();
        final var futures = new Future[upperBound];
        final var executor = Executors.newFixedThreadPool(6);
        final var timer = System.currentTimeMillis();
        for (int i = 0; i < upperBound; i++) {
            final int index = i;
            futures[i] = executor.submit(() -> {
                final var result = checkLineForEmpty(sensors, index, upperBound);
                printfln("Finished %d", index);
                if (index % 10 == 0) {
                    printfln("Took %.4f s", (System.currentTimeMillis() - timer)/1000.0);
                }
                results[index] = result;
                if (-1 + result != upperBound) {
                    printfln("From Thread: ->%d", result);
                    stop.set(true);
                }
            });
        }

        try {
            for (Future future : futures) {
                if (future.isCancelled()) continue;
                if (stop.get()) {
                    print("Canceling all threads!");
                    for (Future f : futures) {
                        f.cancel(true);
                    }
                }
                future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        executor.close();
        return results;
    }

    private int checkLineForEmpty(List<Sensor> sensors, int rowOfInterest, int bound) {
        final var grid = SemiEagerGrid.empty(rowOfInterest);
        sensors.forEach(grid::withSensor);
        final var counter = new AtomicInteger();
        return (int) grid.map().entrySet().stream()
                .filter(it -> it.getKey().y() == rowOfInterest)
                .filter(it -> it.getKey().x() >= 0 && it.getKey().x() <= bound)
                .sorted(Entry.comparingByKey())
                .map(Entry::getKey)
                .takeWhile(it -> it.x == counter.getAndIncrement())
                .count();
    }

    private static Stream<Coordinate> makeSquareCoordinateStreamOfBoundingArea(int upperBound) {
        return IntStream.rangeClosed(0, upperBound)
                .boxed()
                .flatMap(y -> IntStream.rangeClosed(0, upperBound)
                        .boxed()
                        .map(x -> new Coordinate(x, y)))
                .parallel();
    }
}
