package tech.dobler.aoc22;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Day03 {

    public static int letterValue(Character c) {
        return (c < 'a' ? c - 'A' + 26 : c - 'a') + 1;
    }

    public int part1(Stream<String> input) {
        return input
                .map(rucksack ->
                {
                    final var firstHalf = new ArrayList<>(
                            stringToCharList(rucksack.substring(0, rucksack.length() / 2))) {
                    };
                    firstHalf.retainAll(
                            stringToCharList(rucksack.substring(rucksack.length() / 2)));
                    return firstHalf.get(0);
                })
                .mapToInt(Day03::letterValue)
                .sum();
    }

    private static List<Character> stringToCharList(String str) {
        return str
                .chars()
                .mapToObj(e -> (char) e)
                .toList();
    }

    public int part2(String input) {
        String[] split = input.split("\n");
        List<Integer> results = new ArrayList<>(split.length/3);
        for (int i = 0; i < split.length; i+=3) {
            var s1 = new ArrayList<>(stringToCharList(split[i]));
            var s2 = stringToCharList(split[i + 1]);
            var s3 = stringToCharList(split[i + 2]);

            s1.retainAll(s2);
            s1.retainAll(s3);
            results.add(letterValue(s1.get(0)));
        }

        return results.stream().mapToInt(it -> it).sum();
    }
}