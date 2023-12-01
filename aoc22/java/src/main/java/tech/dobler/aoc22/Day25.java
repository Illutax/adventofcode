package tech.dobler.aoc22;

import java.util.Map;
import java.util.stream.Stream;

public class Day25 {

    private static final Map<Character, Long> decoder = Map.of(
            '0', 0L,
            '1', 1L,
            '2', 2L,
            '-', -1L,
            '=', -2L
    );

    private static final Map<Long, String> encoder = Map.of(
            -1L, "-",
            -2L, "=",
            0L, "0",
            1L, "1",
            2L, "2",
            3L, "=1",
            4L, "-1"
    );

    record SNAFU(String encoded) {
        public static SNAFU of(int encoded) {
            return new SNAFU(encode(encoded));
        }

        private static String encode(long number) {
            var result = new StringBuilder();
            var r = number % 5;
            if (r > 3) {
                r -= 5;
                result.append(1).append(encoder.get(r));
            }

            return result.toString();
        }

        public long decoded() {
            long result = 0;
            final var length = encoded().length();
            for (int i = 0; i < length; ++i) {
                final var c = encoded.charAt(i);
                if (!decoder.containsKey(c))
                    throw new IllegalArgumentException("Unknown %s".formatted(c));
                final var aLong = decoder.get(c);
                if (i > 0) result *= 5;
                result += aLong;
            }
            return result;
        }
    }

    public int part1(Stream<SNAFU> input) {
        return -1;
    }

    public int part2(Stream<SNAFU> input) {
        return -1;
    }
}
