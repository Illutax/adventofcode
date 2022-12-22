package tech.dobler.aoc22;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.System.lineSeparator;

public class Day11 {
    @FunctionalInterface
    interface WorriednessTransformer {

        static WorriednessTransformer from(String operator, String operand) {
            final BinaryOperator<WorryLevel> operation = operator.equals("+") ? WorryLevel::sum : WorryLevel::multiply;

            return (previous -> operation.apply(previous, determineRightOperand(operand, previous).get()));
        }

        private static Supplier<WorryLevel> determineRightOperand(String op, WorryLevel prev) {
            return () -> op.equals("old") ? prev : WorryLevelBI.parse(op);
        }

        WorryLevel hype(WorryLevel previous);
    }

    interface WorryLevel {
        BigInteger value();

        WorryLevel sum(WorryLevel other);

        WorryLevel multiply(WorryLevel other);

        WorryLevel mod(long other);

        WorryLevel divide(long i);
    }

    // WorryLevelInt is not big enough when multiplying
    record WorryLevelLong(long internal) implements WorryLevel {

        @Override
        public BigInteger value() {
            return BigInteger.valueOf(internal);
        }

        @Override
        public WorryLevel sum(WorryLevel other) {
            return of(Math.addExact(value().intValue(), other.value().intValue()));
        }

        @Override
        public WorryLevel multiply(WorryLevel other) {
            return of(Math.multiplyExact(value().longValue(), other.value().longValue()));
        }

        @SuppressWarnings("unused")
        public WorryLevel divide(WorryLevel other) {
            return divide(other.value().longValue());
        }

        @Override
        public WorryLevel mod(long other) {
            return of(value().longValue() % other);
        }

        @Override
        public WorryLevel divide(long i) {
            return of(Math.divideExact(value().longValue(), i));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof WorryLevel that)) return false;

            return BigInteger.valueOf(internal).equals(that.value());
        }

        @Override
        public int hashCode() {
            return BigInteger.valueOf(internal).hashCode();
        }

        public static WorryLevel of(long i) {
            return new WorryLevelLong(i);
        }
    }

    record WorryLevelBI(BigInteger internal) implements WorryLevel {
        private static final Map<BigInteger, WorryLevel> cache = new HashMap<>();
        public static final WorryLevel ZERO = WorryLevelBI.of(0);
        public static final WorryLevel ONE = WorryLevelBI.of(1);

        static {
            cache.put(BigInteger.ONE, ONE);
            List.of(2, 3, 5, 7, 8, 13, 17, 19).forEach(it ->
                    cache.put(BigInteger.valueOf(it), WorryLevelBI.of(it)));
        }

        public static WorryLevel of(BigInteger i) {
            return cache.computeIfAbsent(i, WorryLevelBI::new);
        }

        public static WorryLevel of(long i) {
            return of(BigInteger.valueOf(i));
        }

        public static WorryLevel parse(String i) {
            return of(Integer.parseInt(i));
        }

        @Override
        public BigInteger value() {
            return internal;
        }

        @Override
        public WorryLevel sum(WorryLevel other) {
            return of(value().add(other.value()));
        }

        @Override
        public WorryLevel multiply(WorryLevel other) {
            return of(value().multiply(other.value()));
        }

        @Override
        public WorryLevel mod(long other) {
            return WorryLevelBI.of(value().mod(BigInteger.valueOf(other)));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof WorryLevel that)) return false;

            return value().equals(that.value());
        }

        @Override
        public int hashCode() {
            return value().hashCode();
        }

        @Override
        public WorryLevel divide(long i) {
            return divide(WorryLevelBI.of(i));
        }

        private WorryLevel divide(WorryLevel other) {
            return WorryLevelBI.of(value().divide(other.value()));
        }
    }

    @FunctionalInterface
    interface Divisibility {
        boolean test(WorryLevel divisor, int dividend);
    }

    @FunctionalInterface
    interface TransferDecider {
        int test(Divisibility divisibility, WorryLevel worryLevel);
    }

    record Item(WorryLevel level) {
        static Queue<Item> from(String line) {
            return Stream.of(line.split(", "))
                    .map(Integer::parseInt)
                    .map(WorryLevelLong::of)
                    .map(Item::new)
                    .collect(Collectors.toCollection(ArrayDeque::new));
        }
    }

    record Monkey(String id, Queue<Item> items, WorriednessTransformer transformer, TransferDecider decider,
                  Divisibility divisibility, int divisor, boolean isMultiplying) {
        private static final String regex = "^Monkey (\\d):\\s+"
                + "Starting items: ([\\d, ]+)\\s+"
                + "Operation: new = old ([+*]) ([\\w]+)\\s+"
                + "Test: divisible by (\\d+)\\s+"
                + "If true: throw to monkey (\\d)\\s+"
                + "If false: throw to monkey (\\d)\\s?$";
        private static final Pattern pattern = Pattern.compile(regex);

        public static Monkey from(String input) {
            final var result = pattern.matcher(input);
            Util.requirePatternMatches(input, result);
            final var id = result.group(1);
            final var items = result.group(2);

            final var operator = result.group(3);
            final var operand = result.group(4);

            final var divisor = Integer.parseInt(result.group(5));
            final var positive = Integer.parseInt(result.group(6));
            final var negative = Integer.parseInt(result.group(7));

            final var transformer = WorriednessTransformer.from(operator, operand);
            final Divisibility divisibility = (u, d) -> u.mod(d).equals(WorryLevelBI.ZERO);
            final TransferDecider decider = (fn, worryLevel) -> fn.test(worryLevel, divisor) ? positive : negative;

            return new Monkey(id,
                    Item.from(items),
                    transformer,
                    decider,
                    divisibility,
                    divisor,
                    "*".equals(operator) && !"old".equals(operand));
        }

        @Override
        public String toString() {
            return "Monkey %s: %s".formatted(id, prettyItemsList());
        }

        private String prettyItemsList() {
            return items().stream().map(Item::level).map(String::valueOf).collect(Collectors.joining(", "));
        }
    }

    static final class KeepAway {
        private final List<Monkey> monkeys;
        private final Map<Monkey, Integer> handledItemsLedger = new TreeMap<>(Comparator.comparing(Monkey::id));

        @SuppressWarnings("unused")
        private int playedRounds = 0;
        private UnaryOperator<WorryLevel> manageFrustrationFunction;

        KeepAway(List<Monkey> monkeys) {
            this.monkeys = monkeys;
        }

        public KeepAway manageFrustration(UnaryOperator<WorryLevel> fn) {
            this.manageFrustrationFunction = fn;
            return this;
        }

        private void playRound() {
            for (Monkey monkey : monkeys) {
                while (!monkey.items.isEmpty()) {
                    handledItemsLedger.merge(monkey, 1, Integer::sum);
                    final var currentlyInspectingItem = monkey.items.poll();
                    if (currentlyInspectingItem == null) throw new IllegalStateException("Shouldn't happen");
                    var worryLevelAfterInspection = monkey.transformer.hype(currentlyInspectingItem.level());
                    worryLevelAfterInspection = manageFrustrationFunction.apply(worryLevelAfterInspection);
                    final var targetMonkeyIndex = monkey.decider.test(monkey.divisibility, worryLevelAfterInspection);
                    final var itemToInsert = new Item(worryLevelAfterInspection);
                    monkeys.get(targetMonkeyIndex).items.offer(itemToInsert);
                }
            }
            playedRounds++;
        }

        public KeepAway playRounds(int amount) {
            IntStream.range(0, amount).forEach(__ -> this.playRound());
            return this;
        }

        public long getMonkeyBusiness() {
            return handledItemsLedger.values()
                    .stream()
                    .sorted(Comparator.reverseOrder())
                    .limit(2)
                    .mapToLong(it -> it)
                    .reduce(1L, Util.Math::multiply);
        }

        public KeepAway displayLedger(Consumer<String> writer) {
            writer.accept(handledItemsLedger.entrySet()
                    .stream()
                    .map(e -> "Monkey %s inspected items %d times.".formatted(e.getKey().id(), e.getValue()))
                    .collect(Collectors.joining(lineSeparator(),
                            "== After round %d ==%s".formatted(playedRounds, lineSeparator()),
                            lineSeparator())));
            return this;
        }
    }

    public long part1(List<Monkey> monkeys) {
        return new KeepAway(monkeys)
                .manageFrustration(it -> it.divide(3))
                .playRounds(20)
                .getMonkeyBusiness();
    }

    public long part2(List<Monkey> monkeys) {
        final var gdc = monkeys.stream()
                .map(Monkey::divisor)
                .reduce(1, Util.Math::multiply);
        return new KeepAway(monkeys)
                .manageFrustration(it -> it.mod(gdc))
                .playRounds(1)
                .displayLedger(Util::print)
                .playRounds(19)
                .displayLedger(Util::print)
                .playRounds(980)
                .displayLedger(Util::print)
                .playRounds(9_000)
                .displayLedger(Util::print)
                .getMonkeyBusiness();
    }
}