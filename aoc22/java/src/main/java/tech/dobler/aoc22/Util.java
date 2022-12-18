package tech.dobler.aoc22;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class Util {
    private Util() {
    }

    public static Stream<String> splitByNewLine(String input) {
        return Stream.of(input.split("\n"));
    }

    public static <T> Stream<T> splitByNewLine(String testInput, Function<String, T> transformer) {
        return splitByNewLine(testInput).map(transformer);
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

}
