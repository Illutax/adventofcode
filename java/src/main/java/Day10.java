import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;


@Slf4j public class Day10 {
    public static void main(String[] args) {
        final var adapters = AoCUtil.parseInputToListOfInts(10);
        assert adapters != null;
        int result1 = part1(adapters);
        assert result1 == 2176 : "Correct answer";
        log.info("Part1 solution={}", result1);
    }

    public static int part1(List<Integer> adapters) {
        final var changes = Arrays.asList(0, 0, 0, 0);
        adapters.stream().sorted().reduce((a, b) -> {
            changes.set(b - a, changes.get(b - a) + 1);
            return b;
        });
        log.debug("Changes={}",changes);

        return (1 + changes.get(1)) * (1 + changes.get(3));
    }
}
