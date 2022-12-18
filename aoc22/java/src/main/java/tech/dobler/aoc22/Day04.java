package tech.dobler.aoc22;

import java.util.stream.Stream;

public class Day04 {

    record Range(int start, int end) {
        Range(String str) {
            this(Integer.parseInt(str.split("-")[0]), Integer.parseInt(str.split("-")[1]));
        }
    }

    record Ranges(Range left, Range right) {
        Ranges(String str) {
            this(new Range(str.split(",")[0]), new Range(str.split(",")[1]));
        }

        public int includedFully() {
            return left.start <= right.start && left.end >= right.end
                    || right.start <= left.start && right.end >= left.end
                    ? 1
                    : 0;
        }

        public int intersects() {
            return left.start <= right.start && right.start <= left.end
                    || left.start <= right.end && right.end <= left.end
                    || right.start <= left.start && left.start <= right.end
                    || right.start <= left.end && left.end <= right.end
                    ? 1
                    : 0;
        }
    }

    public int part1(Stream<String> input) {
        return input.map(Ranges::new).mapToInt(Ranges::includedFully).sum();
    }

    public int part2(Stream<String> input) {
        return input.map(Ranges::new).mapToInt(Ranges::intersects).sum();
    }
}