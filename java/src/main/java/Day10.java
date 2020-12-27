import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j public class Day10 {
    static Map<Integer, Long> dp;

    public static void main(String[] args) {
        final var adapters = AoCUtil.parseInputToIntList(10);
        int result1 = part1(adapters);
        assert result1 == 2176 : "Correct answer";
        log.info("Part1 solution={}", result1);

        long result2 = part2(adapters);
        assert result2 == 18512297918464L : "Correct answer";
        log.info("Part2 solution={}", result2);
    }

    public static int part1(List<Integer> adapters) {
        final var changes = new int[]{ 0, 0, 0, 0};
        adapters.stream().sorted().reduce((a, b) -> {
            changes[b - a]++;
            return b;
        });
        return (1 + changes[1]) * (1 + changes[3]);
    }

    public static long part2(List<Integer> adapters) {
        dp = new HashMap<>();
        adapters = new ArrayList<>(adapters);
        adapters.sort(Comparator.naturalOrder());
        adapters.add(0, 0);
        adapters.add(adapters.get(adapters.size() - 1) + 3);
        return doSolve(adapters, 0);
    }

    private static long doSolve(List<Integer> xs, int i) {
        if (i == xs.size() - 1) return 1;
        if (dp.containsKey(i)) return dp.get(i);
        long ans = 0;
        for (int j = i + 1; j < xs.size(); ++j) {
            if (xs.get(j) - xs.get(i) <= 3) ans += doSolve(xs, j);
        }
        dp.put(i, ans);
        return ans;
    }

}
