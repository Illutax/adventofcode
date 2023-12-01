import java.util.*;
import java.util.stream.Stream;

public class Day01 {

    public int part1(Stream<String> input) {
        return input.mapToInt(this::parseLine).sum();
    }

    private int parseLine(String line) {
        final var digits = Arrays.stream(line.split(""))
                .filter(c -> Character.isDigit(c.charAt(0)))
                .toList();
        return Integer.parseInt(digits.getFirst() + digits.getLast());
    }

    public int part2(Stream<String> input) {
        return part1(input.map(this::preprocess));
    }

    Map<String, String> map = Map.of(
            "one", "1","two", "2","three", "3",
            "four", "4","five", "5","six", "6",
            "seven", "7","eight", "8","nine", "9"
    );

    String preprocess(String line) {
        final var sb = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            final var maybeNewIndex = tryParse(line, i, sb);
            if (maybeNewIndex != i) i += maybeNewIndex;
            else sb.append(line.charAt(i));
        }
        return sb.toString();
    }

    private int tryParse(String line, int index, StringBuilder sb) {
        int found = -1;
        for (var entry : this.map.entrySet()) {
            int keyLength = entry.getKey().length() - 1;
            boolean inbounds = index + keyLength < line.length();
            if (inbounds && line.substring(index, index + keyLength + 1).equals(entry.getKey())) {
                sb.append(entry.getValue());
                found = keyLength-1; // should have been only "keyLength" but because of overlap i don't want to mess with it anymore...
                break;
            }
        }
        return found > -1 ? found : index;
    }
}