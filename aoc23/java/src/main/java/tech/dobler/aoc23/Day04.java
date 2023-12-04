package tech.dobler.aoc23;

import lombok.NonNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day04 {

    public int part1(Stream<String> input) {
        return input.map(Card::of)
                .map(Card::amountOfWinningNumbers)
                .mapToInt(integers -> (int) Math.pow(2, integers - 1d))
                .sum();
    }

    record Card(int id, int amountOfWinningNumbers) {
        public static final Pattern PATTERN = Pattern.compile("Card +(\\d+): ((?: ?\\d+ )*+)\\|((?: ? ?\\d+)++)");

        public static Card of(String line) {
            Matcher matcher = PATTERN.matcher(line);
            if (!matcher.matches()) throw new IllegalStateException("Couldn't match card with '%s'".formatted(line));
            if (matcher.groupCount() != 3)
                throw new IllegalStateException("Expected three groups but got %d".formatted(matcher.groupCount()));
            int id = Integer.parseInt(matcher.group(1));
            Set<Integer> winning = makeSet(matcher.group(2));
            Set<Integer> my = makeSet(matcher.group(3));
            int amountOfWinningNumbers = (int) winning.stream()
                    .filter(my::contains)
                    .count();
            return new Card(id, amountOfWinningNumbers);
        }

        private static Set<Integer> makeSet(String numbers) {
            return Arrays.stream(numbers.split(" "))
                    .filter(Predicate.not(String::isBlank))
                    .map(Integer::parseInt)
                    .collect(Collectors.toUnmodifiableSet());
        }
    }

    public int part2(Stream<String> input) {
        var cards = input.map(Card::of)
                .collect(Collectors.toMap(Card::id, Function.identity()));
        int size = cards.keySet().size();
        final int[] winningCards = {0};
        Deque<Card> countingQueue = new ArrayDeque<>(cards.values()) {
            @Override
            public void addLast(@NonNull Card card) {
                winningCards[0]++;
                super.addLast(card);
            }
        };

        while (!countingQueue.isEmpty()) {
            Card poll = countingQueue.poll();
            IntStream.range(0, poll.amountOfWinningNumbers()).forEach(i -> {
                int next = poll.id + i + 1;
                if (next <= size) {
                    countingQueue.addLast(cards.get(next));
                }
            });
        }
        return winningCards[0];
    }
}