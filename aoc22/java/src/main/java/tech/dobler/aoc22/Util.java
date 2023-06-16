package tech.dobler.aoc22;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Util {
    private Util() {
    }

    public static Stream<String> splitByNewLine(String input) {
        return Stream.of(input.split("\n"));
    }

    public static Stream<String> splitBy(String regex, String input) {
        return Stream.of(input.split(regex));
    }

    public static <T> Stream<T> splitByNewLine(String testInput, Function<String, T> transformer) {
        return splitBy("\n", testInput).map(transformer);
    }

    public static <T> Stream<T> splitByDoubleNewLine(String testInput, Function<String, T> transformer) {
        return splitBy("\n\n", testInput).map(transformer);
    }

    @SuppressWarnings("unused")
    public static <T> Stream<T> splitBy(String regex, String testInput, Function<String, T> transformer) {
        return splitBy(regex, testInput).map(transformer);
    }

    public static String readInput(int day) {
        try {
            return Files.readString(new File("../input/day%02d.txt".formatted(day)).toPath());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Stream<String> readLines(int day) {
        try {
            return Files.lines(new File("../input/day%02d.txt".formatted(day)).toPath());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static List<String> splitToList(String input) {
        return Arrays.asList(input.split("\n"));
    }

    public static List<String> splitToList(int day) {
        return Arrays.asList(readInput(day).split("\n"));
    }

    public static void print(String str) {
        System.out.println(str); // NOSONAR
    }

    public static void printf(String str, Object... args) {
        System.out.printf(str, args); // NOSONAR
    }
    public static void printfln(String str, Object... args) {
        System.out.printf(str+"\n", args); // NOSONAR
    }
    public static void requirePatternMatches(String input, Matcher result) {
        if (!result.matches()) throw new PatternDoesNotMatchException("Does not match pattern \"%s\" for regex: \"%s\"".formatted(input, result.pattern().pattern()));
    }

    public static <T> Stream<List<T>> chunked(List<T> list, int size) {
        return chunked(list.stream(), size);
    }

    public static <T> Stream<List<T>> chunked(Stream<T> stream, int size) {
        final var counter = new AtomicInteger();
        return stream.collect(Collectors.groupingBy(x -> counter.getAndIncrement() / size))
                .values().stream();
    }

    static class PatternDoesNotMatchException extends IllegalArgumentException {
        public PatternDoesNotMatchException(String input) {
            super("Does not match pattern \"%s\"".formatted(input));
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Math {

        public static int multiply(int a, int b) {
            return a * b;
        }
        public static long multiply(long a, long b) {
            return a * b;
        }
    }
}
