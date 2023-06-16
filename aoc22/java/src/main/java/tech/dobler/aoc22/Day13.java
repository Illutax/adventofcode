package tech.dobler.aoc22;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;

public class Day13 {
    public sealed interface Packet extends Comparable<Packet> permits Single, Multiple {
    }

    public record Single(int value) implements Packet {
        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @Override
        public int compareTo(Packet o) {
            if (o instanceof Single that) {
                return Integer.compare(this.value, that.value);
            }

            final var that = (Multiple) o;
            // elevate this to be in a Packets
            final var newThis = new Multiple(List.of(this));
            // and retry comparison
            return newThis.compareTo(that);
        }
    }

    public record Multiple(List<Packet> entries) implements Packet, Comparable<Packet> {

        public static Multiple parseLine(String line) {
            return parseInternal(line, new AtomicInteger(1));
        }

        private static Multiple parseInternal(String line, AtomicInteger startingIndex) {
            final var result = new ArrayList<Packet>();
            final var max = line.length() - 1;
            while (startingIndex.get() < max) {
                int i = startingIndex.getAndIncrement();
                final var c0 = line.charAt(i);
                if (c0 == ']') break;
                else if (c0 == '[') {
                    final var out = parseInternal(line, startingIndex);
                    result.add(out);
                } else if (c0 != ',') {
                    final var sb = new StringBuilder();
                    for (; i < max; i++) {
                        final var c = line.charAt(i);
                        if (c == ',' || c == '[' || c == ']') break;
                        sb.append(c);
                    }
                    result.add(new Single(Integer.parseInt(sb.toString())));

                }
            }
            return new

                    Multiple(result);
        }

        @Override
        public String toString() {
            return entries.toString();
        }

        @Override
        public int compareTo(Packet o) {
            if (!(o instanceof Multiple others)) {
                // other is single, then we try to compare the inverse to only convert in one place and invert the result
                return o.compareTo(this) * -1;
            }
            final var left = entries;
            final var right = others.entries;

            for (int i = 0; i < Math.min(left.size(), right.size()); i++) {
                final var l = left.get(i);
                final var r = right.get(i);
                final var result = l.compareTo(r);
                if (result != 0) return result;
            }
            return Integer.compare(left.size(), right.size());
        }
    }

    public record PacketPair(Multiple left, Multiple right) {
        public PacketPair {
            Objects.requireNonNull(left);
            Objects.requireNonNull(right);
        }

        public int compareComponents() {
            return left.compareTo(right);
        }
    }

    public static List<PacketPair> parseAsPairs(String input) {
        return Util.chunked(parseToList(input), 2)
                .map(l -> new PacketPair(l.get(0), l.get(1)))
                .toList();
    }

    public static List<Multiple> parseToList(String input) {
        return Arrays.stream(input.split("\n"))
                .filter(line -> !line.isBlank())
                .map(Multiple::parseLine)
                .toList();
    }

    public int part1(List<PacketPair> pairs) {
        final var counter = new AtomicInteger();
        final UnaryOperator<Integer> countIfSortedCorrectly = it -> it < 0 ? 1 : 0;
        return pairs.stream()
                .map(PacketPair::compareComponents)
                .map(countIfSortedCorrectly)
                .map(it -> counter.incrementAndGet() * it)
                .mapToInt(i -> i)
                .sum();
    }

    public int part2(List<Multiple> packages) {
        final var result = new ArrayList<Packet>(packages.size() + 2);
        result.addAll(packages);
        final var dividerPackages = List.of(Multiple.parseLine("[[2]]"), Multiple.parseLine("[[6]]"));
        result.addAll(dividerPackages);
        result.sort(Packet::compareTo);
        final var i1 = result.indexOf(dividerPackages.get(0));
        final var i2 = result.indexOf(dividerPackages.get(1));
        return (i1 + 1) * (i2 + 1);
    }
}
