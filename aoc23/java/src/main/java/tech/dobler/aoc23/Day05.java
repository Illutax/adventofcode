package tech.dobler.aoc23;

import java.util.*;

public class Day05 {
    private static final int AMOUNT_OF_TRANSFORMATIONS = 7;

    public long part1(List<String> input) {
        final var singleIntervals = getSeeds(input.get(0)).stream()
                .map(seed -> Interval.of(seed, 1))
                .toList();
        return almanac(input, singleIntervals).stream()
                .min(Comparator.naturalOrder()).orElseThrow();
    }

    public static List<Long> getSeeds(String line) { // NOSONAR
        return Arrays.stream(line.split(": ")[1]
                        .split(" "))
                .map(Long::parseLong)
                .toList();
    }


    @SuppressWarnings("java:S127")
    private static List<Long> almanac(List<String> input, List<Interval> seeds) {
        final long initialSummedLength = seeds.stream().mapToLong(Interval::length).sum();
        seeds = new ArrayList<>(seeds);
        for (int index = 2, i = 0; i < AMOUNT_OF_TRANSFORMATIONS; i++, index++) {
            List<Interval> mappedSeeds = new ArrayList<>();
            while (++index < input.size() && !input.get(index).isBlank()) {
                final var transformation = Transformation.of(input.get(index));

                final Queue<Interval> queue = new ArrayDeque<>(seeds);
                while (!queue.isEmpty()) {
                    final var seed = queue.poll();
                    if (transformation.intersects(seed)) {
                        final var parts = seed.split(transformation.interval);
                        final var intersection = Objects.requireNonNull(parts.poll());
                        final var mappedTo = intersection.translate(transformation.delta);
                        mappedSeeds.add(mappedTo);
                        seeds.remove(seed);
                        seeds.addAll(parts);
                    }
                }
            }
            seeds.addAll(mappedSeeds);
        }

        final long finalSummedLength = seeds.stream().mapToLong(Interval::length).sum();
        if (initialSummedLength != finalSummedLength)
            throw new IllegalStateException("[Sanity check] expected: %d got: %d".formatted(initialSummedLength, finalSummedLength));
        return seeds.stream().map(Interval::start).toList();
    }

    record Transformation(Interval interval, long delta) {
        public static Transformation of(String input) {
            final var line = input.split(" ");
            final long destination = Long.parseLong(line[0]);
            final long source = Long.parseLong(line[1]);
            final long length = Long.parseLong(line[2]);
            return new Transformation(
                    new Interval(source, source + length),
                    source - destination
            );
        }

        public boolean intersects(Interval other) {
            return other.start() < interval.end && other.end() > interval.start;
        }
    }

    public long part2(List<String> input) {
        final var seeds = interpretAsIntervals(getSeeds(input.get(0)));
        return almanac(input, seeds).stream()
                .min(Comparator.naturalOrder()).orElseThrow();
    }

    private static List<Interval> interpretAsIntervals(List<Long> seedRanges) {
        if (seedRanges.size() % 2 != 0)
            throw new IllegalArgumentException("seedRanges should be of even length %s".formatted(seedRanges));
        return Util.chunked(seedRanges, 2)
                .map(r -> Interval.of(r.getFirst(), r.getLast() - 1))
                .toList();
    }

    record Interval(long start, long end) {
        Interval {
            if (start == end) throw new IllegalArgumentException("Empty Interval at %d".formatted(start));
        }

        public static Interval of(long start, long length) {
            return new Interval(start, start + length);
        }

        public Interval translate(long delta) {
            return new Interval(start - delta, end - delta);
        }

        public long length() {
            return end - start;
        }

        public Queue<Interval> split(Interval seed) {
            if (seed.start() <= start() && seed.end() >= end()) return new ArrayDeque<>(List.of(this));
            Deque<Interval> result = splitInternal(seed);

            long newSeedLength = result.stream().mapToLong(Interval::length).sum();
            if (newSeedLength > this.length())
                throw new IllegalStateException("We created length expected: %d got: %d".formatted(this.length(), newSeedLength));
            if (newSeedLength < this.length())
                throw new IllegalStateException("We lost length expected: %d got: %d".formatted(this.length(), newSeedLength));
            return result;
        }

        private Deque<Interval> splitInternal(Interval seed) {
            Deque<Interval> result = new ArrayDeque<>();

            var starting = start();
            // Cutting the left edge
            if (seed.start() > start()) {
                result.add(new Interval(start(), seed.start()));
                starting = seed.start();
            }

            // exceeding the right edge
            if (seed.end() > end()) {
                result.addFirst(new Interval(starting, end()));
            } else {
                result.addFirst(new Interval(starting, seed.end()));
                if (seed.end() != end()) result.add(new Interval(seed.end(), end()));
            }
            return result;
        }
    }

}