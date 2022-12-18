package tech.dobler.aoc22;

import java.util.Comparator;
import java.util.stream.Stream;

public class Day01
{
    public int part1(String input)
    {
        return Stream.of(input.split("\n\n"))
                .map(elf -> Stream.of(elf.split("\n"))
                        .mapToInt(Integer::parseInt)
                        .sum())
                .max(Comparator.naturalOrder())
                .orElseThrow();
    }

    public int part2(String input) {
        return Stream.of(input.split("\n\n"))
                .map(elf -> Stream.of(elf.split("\n"))
                        .mapToInt(Integer::parseInt)
                        .sum())
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .mapToInt(it -> it)
                .sum();
    }
}