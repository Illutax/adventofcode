package tech.dobler.aoc22;

import java.util.ArrayList;
import java.util.List;

public class Day06 {
    public int part1(String input) {
        return probe(input, 4);
    }

    private int probe(String input, int desiredLength) {
        var uniquePrefix = new ArrayList<Character>();
        for (int i = 0; i < input.length(); i++) {
            final var c = input.charAt(i);
            clearPrefixIfNecessary(uniquePrefix, c);
            uniquePrefix.add(c);
            if (uniquePrefix.size() == desiredLength) return i + 1;
        }

        throw new IllegalStateException("Should never get here");
    }

    private static void clearPrefixIfNecessary(List<Character> chars, char c) {
        for (int i = 0; i < chars.size(); ++i) {
            if (chars.get(i) == c) {
                chars.subList(0, i + 1).clear();
                return;
            }
        }
    }

    public int part2(String input) {
        return probe(input, 14);
    }
}