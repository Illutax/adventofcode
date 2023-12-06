package tech.dobler.aoc23;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Day06 {
    public long part1(String input) {
        return Race.ofPart1(input).stream()
                .map(Race::winningAccelerations)
                .reduce(1L, Util.Math::multiply);
    }

    public long part2(String input) {
        return Race.ofPart2(input).winningAccelerations();
    }

    record Race(long ms, long mm) {
        public static List<Race> ofPart1(String input) {
            var lines = input.split("\n");
            final var times = lines[0].split(":")[1].split("\\s+");
            final var dists = lines[1].split(":")[1].split("\\s+");
            return IntStream.range(1, times.length)
                    .mapToObj(i -> new Race(Integer.parseInt(times[i]), Integer.parseInt(dists[i])))
                    .toList();
        }
        public static Race ofPart2(String input) {
            var lines = input.split("\n");
            final var time = String.join("", lines[0].split(":")[1].split("\\s+"));
            final var dist =  String.join("", lines[1].split(":")[1].split("\\s+"));
            return new Race(Long.parseLong(time), Long.parseLong(dist));
        }

        public long winningAccelerations() {
            return LongStream.range(1, ms)
                    .map(i -> i * (ms - i))
                    .filter(i -> i > mm)
                    .count();
        }
    }
}