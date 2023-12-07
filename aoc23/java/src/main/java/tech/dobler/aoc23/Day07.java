package tech.dobler.aoc23;

import lombok.NonNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

public class Day07 {

    public long part1(Stream<String> input) {
        final var counter = new AtomicLong(1);
        return input.map(Hand::of)
                .sorted()
                .map(Hand::bet)
                .collect(Collectors.toMap(_ -> counter.getAndIncrement(), Function.identity()))
                .entrySet().stream()
                .mapToLong(entry -> entry.getKey() * entry.getValue())
                .sum();
    }

    record Hand(List<Strength> cards, Type type, int bet) implements Comparable<Hand> {
        private static final Pattern PATTERN = Pattern.compile("([2-9TJQKA]{5}) (\\d+)");

        public static Hand of(String line) {
            final var matcher = PATTERN.matcher(line);
            Util.requirePatternMatches(line, matcher);
            final var suites = matcher.group(1);
            final var bet = Integer.parseInt(matcher.group(2));
            final var cards = Arrays.stream(suites.split(""))
                    .map(Strength::of)
                    .toList();
            return new Hand(cards, Type.of(cards), bet);
        }

        //        @Override
//        public int compareToLOL(@NonNull Hand other) {
//            var c = Comparator.comparing(Hand::type);
//            for (int i = 0; i < 5; i++) {
//                final var j = i;
//                c = c.thenComparing(o -> o.cards.get(j));
//            }
//            return c.compare(this, other);
//        }
        @Override
        public int compareTo(@NonNull Hand other) {
            final var result = Comparator.comparing(Hand::type)
                    .thenComparing(o -> o.cards.get(0))
                    .thenComparing(o -> o.cards.get(1))
                    .thenComparing(o -> o.cards.get(2))
                    .thenComparing(o -> o.cards.get(3))
                    .thenComparing(o -> o.cards.get(4))
                    .compare(this, other);
            return result;
        }
    }

    record Type(int order, String value, Predicate<List<Strength>> p) implements Comparable<Type> {
        public static final Type ONE_PAIR = new Type(6, "One pair", hand -> streamOfGroupedStrengths(hand).filter(g -> g.size() == 2).count() == 1);
        public static final Type TWO_PAIRS = new Type(5, "Two pairs", hand -> streamOfGroupedStrengths(hand).filter(g -> g.size() == 2).count() == 2);
        public static final Type THREE_OF_A_KIND = new Type(4, "Three of a kind", hand -> streamOfGroupedStrengths(hand).anyMatch(groups -> groups.size() == 3));
        public static final Type FOUR_OF_A_KIND = new Type(2, "Four of a kind", hand -> streamOfGroupedStrengths(hand).anyMatch(groups -> groups.size() == 4));
        public static final Type FIVE_OF_A_KIND = new Type(1, "Five of a kind", hand -> streamOfGroupedStrengths(hand).anyMatch(groups -> groups.size() == 5));
        public static final Type FULL_HOUSE = new Type(3, "Full house", THREE_OF_A_KIND.p.and(ONE_PAIR.p));
        public static final Type HIGH_CARD = new Type(7, "Full house", _ -> true);


        private static Stream<List<Strength>> streamOfGroupedStrengths(List<Strength> hand) {

            return hand.stream()
                    .collect(Collectors.groupingBy(Strength::value,
                            mapping(Function.identity(), toList())))
                    .values().stream();
        }

        public static Type of(List<Strength> hand) {
            final Type result;
            if (FIVE_OF_A_KIND.p.test(hand)) result = FIVE_OF_A_KIND;
            else if (FOUR_OF_A_KIND.p.test(hand)) result = FOUR_OF_A_KIND;
            else if (FULL_HOUSE.p.test(hand)) result = FULL_HOUSE;
            else if (THREE_OF_A_KIND.p.test(hand)) result = THREE_OF_A_KIND;
            else if (TWO_PAIRS.p.test(hand)) result = TWO_PAIRS;
            else if (ONE_PAIR.p.test(hand)) result = ONE_PAIR;
            else result = HIGH_CARD;
            return result;
        }

        @Override
        public int compareTo(@NonNull Day07.Type other) {
            return Comparator.comparing(Type::order).compare(other, this);
        }
    }

    record Strength(String key, int value) implements Comparable<Strength> {

        private static final Map<String, Strength> cards = new HashMap<>();

        static {
            cards.put("2", new Strength("2", 1));
            cards.put("3", new Strength("3", 2));
            cards.put("4", new Strength("4", 2 << 1));
            cards.put("5", new Strength("5", 2 << 2)); // NOSONAR
            cards.put("6", new Strength("6", 2 << 3));
            cards.put("7", new Strength("7", 2 << 4));
            cards.put("8", new Strength("8", 2 << 5));
            cards.put("9", new Strength("9", 2 << 6));
            cards.put("T", new Strength("T", 2 << 7));
            cards.put("J", new Strength("J", 2 << 8));
            cards.put("Q", new Strength("Q", 2 << 9));
            cards.put("K", new Strength("K", 2 << 10));
            cards.put("A", new Strength("A", 2 << 11));
        }

        public static Strength of(String key) {
            return cards.get(key);
        }

        @Override
        public int compareTo(@NonNull Strength other) {
            return Comparator.comparing(Strength::value).compare(this, other);
        }
    }

    public long part2(Stream<String> input) {
        final var counter = new AtomicLong(1);
        return input.map(Hand::of)
                .sorted()
                .map(Hand::bet)
                .collect(Collectors.toMap(_ -> counter.getAndIncrement(), Function.identity()))
                .entrySet().stream()
                .mapToLong(entry -> entry.getKey() * entry.getValue())
                .sum();
    }
}