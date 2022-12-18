package tech.dobler.aoc22;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collector;

public class Day05 {

    static Pattern part1Patten = Pattern.compile("^move (\\d+) from (\\d+) to (\\d+)");

    record Command(int amount, int from, int to) {
        static Command from(String text) {
            final var matcher = part1Patten.matcher(text);
            if (!matcher.find())
                throw new IllegalStateException("Found no match for " + text);
            return new Command(Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3)));
        }
    }

    public String part1(String input) {
        final var stacks = blankStacks();
        final var lines = input.split("\n");
        int j = initStacks(stacks, lines);
        j += 2; // skip empty line

        solveSequentially(stacks, lines, j);
        return readTopCrates(stacks);
    }

    private static void solveSequentially(List<ArrayDeque<Character>> stacks, String[] lines, int j) {
        while (j < lines.length) {
            var line = lines[j++];
            final var command = Command.from(line);
            var from = stacks.get(command.from - 1);
            var to = stacks.get(command.to - 1);
            for (int i = 0; i < command.amount; ++i) {
                to.push(from.pop());
            }
        }
    }

    private static String readTopCrates(List<ArrayDeque<Character>> stacks) {
        return stacks.stream()
                .filter(stack -> !stack.isEmpty())
                .map(Deque::pop)
                .collect(Collector.of(
                        StringBuilder::new,
                        StringBuilder::append,
                        StringBuilder::append,
                        StringBuilder::toString));
    }

    private static int initStacks(List<ArrayDeque<Character>> stacks, String[] lines) {
        String line = lines[0];
        var j = 0;
        while (!line.trim().equals("")) {
            for (int i = 1, k = 0; i < line.length(); i += 4, k++) {
                char letter = line.charAt(i);
                if (letter >= '1' && letter <= '9') return j;
                if (letter != ' ')
                    stacks.get(k).addLast(letter);
            }
            line = lines[++j];
        }
        return j;
    }

    public String part2(String input) {
        final var stacks = blankStacks();
        final var lines = input.split("\n");
        int j = initStacks(stacks, lines);
        j += 2; // skip empty line

        solveSmart(stacks, lines, j);
        return readTopCrates(stacks);
    }

    private static List<ArrayDeque<Character>> blankStacks() {
        return List.of(
                new ArrayDeque<>(),
                new ArrayDeque<>(),
                new ArrayDeque<>(),

                new ArrayDeque<>(),
                new ArrayDeque<>(),
                new ArrayDeque<>(),

                new ArrayDeque<>(),
                new ArrayDeque<>(),
                new ArrayDeque<>()
        );
    }

    private void solveSmart(List<ArrayDeque<Character>> stacks, String[] lines, int j) {
        while (j < lines.length) {
            var line = lines[j++];
            final var command = Command.from(line);
            var from = stacks.get(command.from - 1);
            var to = stacks.get(command.to - 1);
            var tmp = new ArrayDeque<Character>(command.amount);
            for (int i = 0; i < command.amount; ++i) {
                tmp.push(from.pop());
            }
            for (int i = 0; i < command.amount; ++i) {
                to.push(tmp.pop());
            }
        }
    }
}