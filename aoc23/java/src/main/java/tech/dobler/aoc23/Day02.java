package tech.dobler.aoc23;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day02 {

    public int part1(Stream<String> input) {
        return solve(input, new Bag(12, 13, 14));
    }

    private int solve(Stream<String> input, Bag configuration) {
        return input.map(Game::of)
                .filter(game -> game.possible(configuration))
                .mapToInt(Game::id)
                .sum();
    }

    record Game(int id, List<Bag> sets) {
        public static final Pattern PATTERN = Pattern.compile("Game (\\d+): (.*)");

        public static Game of(String line) {
            Matcher matcher = PATTERN.matcher(line);
            if (!matcher.matches()) throw new IllegalStateException("Shouldn't happen");
            String id = matcher.group(1);
            String sets = matcher.group(2);
            return new Game(Integer.parseInt(id), Arrays.stream(sets.split("; ")).map(Bag::of).toList());
        }

        public boolean possible(Bag configuration) {
            return sets.stream().allMatch(bag -> bag.possible(configuration));
        }

        public int power() {
            Bag lgcc = sets.stream().reduce(Bag.EMPTY, Bag::leastGreatestCommonConfiguration);
            int product = lgcc.reds() * lgcc.greens() * lgcc.blues();
            if (product == 0) throw new IllegalStateException("Got a zero in " + lgcc);
            return product;
        }
    }

    record Bag(int reds, int greens, int blues) {
        public static final Pattern PATTERN = Pattern.compile("(\\d+) (\\w+)(?:, (\\d+) (\\w+))?(?:, (\\d+) (\\w+))?");
        public static final Bag EMPTY = new Bag(0, 0, 0);

        public static Bag of(String set) {
            Matcher matcher = PATTERN.matcher(set);
            Util.requirePatternMatches(set, matcher);
            int red = 0;
            int green = 0;
            int blue = 0;
            for (int i = 1; i <= matcher.groupCount() && matcher.group(i) != null; i += 2) {
                final var color = matcher.group(i + 1);
                final int amount = Integer.parseInt(matcher.group(i));
                switch (color) {
                    case "red" -> red += amount;
                    case "green" -> green += amount;
                    case "blue" -> blue += amount;
                    default -> throw new IllegalStateException("Unexpected color: " + color + " " + amount);
                }
            }

            return new Bag(red, green, blue);
        }

        public boolean possible(Bag configuration) {
            return reds <= configuration.reds()
                    && greens <= configuration.greens()
                    && blues <= configuration.blues();
        }

        public Bag leastGreatestCommonConfiguration(Bag other) {
            return new Bag(
                    Math.max(this.reds, other.reds()),
                    Math.max(this.greens, other.greens()),
                    Math.max(this.blues, other.blues()));
        }
    }

    public int part2(Stream<String> input) {
        return input.map(Game::of)
                .mapToInt(Game::power)
                .sum();
    }
}