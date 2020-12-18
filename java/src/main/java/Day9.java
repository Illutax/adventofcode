import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.buffer.CircularFifoBuffer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SuppressWarnings("unchecked")
public class Day9 {
    private static final String PATH = "java/input9.txt";

    public static void main(String[] args) throws IOException {
        var result = part1();
        part2(result);
    }

    private static void part2(int number) {
        Collection<Long> numbers = parseInputToListOfLongs();
        var sum = getSumOfSummedSubsequence(numbers, number);
        assert sum == 1766397 : "correct answer";
    }

    private static List<Long> parseInputToListOfLongs() {
        try {
            return Files.lines(Paths.get(PATH))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int part1() throws IOException {
        final CircularFifoBuffer buffer = new CircularFifoBuffer(25);
        int result = -1;
        try (BufferedReader br = new BufferedReader(new FileReader(new File(PATH)))) {
            for (String line; (line = br.readLine()) != null; ) {
                final var data = Integer.parseInt(line);
                if (buffer.size() < buffer.maxSize() || areTwoDifferentAddendsInPreamble(buffer, data))
                    buffer.add(data);
                else {
                    result = data;
                    break;
                }
            }
        }

        assert result == 14144619 : "Correct answer";
        log.info("Part1 result={}\n", result);

        return result;
    }

    public static boolean areTwoDifferentAddendsInPreamble(Collection<Integer> preamble, int number) {
        return preamble.stream().anyMatch(data1 -> preamble.contains(number - data1) && data1 != number - data1);
    }

    public static long getSumOfSummedSubsequence(Collection<Long> preamble, int number) {
        int l = 0, r = 0;
        final int size = preamble.size();
        while (l < size) {
            var sum = consecutiveSum(preamble, l, r);
            if (sum == number) {
                var range = preamble.stream().skip(l).limit(r-l).collect(Collectors.toList());
                var min = range.stream().min(Comparator.naturalOrder()).orElseThrow();
                var max = range.stream().max(Comparator.naturalOrder()).orElseThrow();
                log.info("Has sequence l={}, r={}, sum={}", min, max, min+max);
                return min+max;
            } else if (sum < number) {
                // it's not in it.
                if (r == size-1) {
                    log.error("It's not in it!");
                    return Long.MIN_VALUE;
                }
                r++;
            } else {
                l++;
            }
        }
        return Long.MIN_VALUE;
    }

    public static boolean hasPreambleSummedSubsequence(Collection<Long> preamble, int number) {
        int l = 0, r = 0;
        final int size = preamble.size();
        while (l < size) {
            var sum = consecutiveSum(preamble, l, r);
            if (sum == number) {
                var range = preamble.stream().skip(l).limit(r-l).collect(Collectors.toList());
                var min = range.stream().min(Comparator.naturalOrder()).orElseThrow();
                var max = range.stream().max(Comparator.naturalOrder()).orElseThrow();
                log.info("WRONG! l={}, r={}, sum={}", l, r, l + r);
                log.info("Has sequence l={}, r={}, sum={}", min, max, min+max);
                return true;
            } else if (sum < number) {
                // it's not in it.
                if (r == size-1) {
                    log.error("It's not in it!");
                    return false;
                }
                r++;
            } else {
                l++;
            }
        }
        return false;
    }

    static long consecutiveSum(Collection<Long> preamble, int l, int r) {
        if (l == r)
            return (long) preamble.toArray()[l];
        var arr = preamble.toArray(new Long[0]);
        long sum = arr[l];
        while (l < r) {
            l++;
            if (l == arr.length) break;
            sum += arr[l];
        }
        return sum;
    }
}
