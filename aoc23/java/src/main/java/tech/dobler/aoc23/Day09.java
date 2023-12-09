package tech.dobler.aoc23;

import java.util.*;
import java.util.stream.Stream;

public class Day09 {

    public long part1(Stream<String> input) {
        return input.map(History::of)
                .mapToLong(History::predictNext)
                .sum();
    }

    record History(List<Long> values) {

        public static History of(String line) {
            return new History(Arrays.stream(line.split(" ")).map(Long::parseLong).toList());
        }

        private History differentiate() {
            var derivative = new ArrayList<Long>(values.size() - 1);
            for (int i = 1; i < values.size(); i++) {
                derivative.add(values.get(i) - values.get(i - 1));
            }
            return new History(derivative);
        }

        public long predictNext() {
            return differentiateToZero().stream()
                    .map(History::values)
                    .map(SequencedCollection::getLast)
                    .reduce(0L, Long::sum);
        }

        public long predictPrevious() {
            return differentiateToZero().stream()
                    .map(History::values)
                    .map(SequencedCollection::getFirst)
                    .reduce(0L, Util.Math::difference);
        }

        private ArrayDeque<History> differentiateToZero() {
            var datasets = new ArrayDeque<History>();
            var h = this;
            do {
                datasets.addFirst(h);
                h = h.differentiate();
            } while (!h.allZeros());
            return datasets;
        }

        private boolean allZeros() {
            return values.stream().allMatch(n -> n == 0);
        }
    }

    public long part2(Stream<String> input) {
        return input.map(History::of)
                .mapToLong(History::predictPrevious)
                .sum();
    }
}