package tech.dobler.aoc22;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day11Test {

    final String testInput = """
            Monkey 0:
              Starting items: 79, 98
              Operation: new = old * 19
              Test: divisible by 23
                If true: throw to monkey 2
                If false: throw to monkey 3
                        
            Monkey 1:
              Starting items: 54, 65, 75, 74
              Operation: new = old + 6
              Test: divisible by 19
                If true: throw to monkey 2
                If false: throw to monkey 0
                        
            Monkey 2:
              Starting items: 79, 60, 97
              Operation: new = old * old
              Test: divisible by 13
                If true: throw to monkey 1
                If false: throw to monkey 3
                        
            Monkey 3:
              Starting items: 74
              Operation: new = old + 3
              Test: divisible by 17
                If true: throw to monkey 0
                If false: throw to monkey 1
            """;
    private Day11 day;

    @BeforeEach
    void setUp() {
        day = new Day11();
    }

    private static List<Day11.Monkey> readInput() {
        return Util.splitByDoubleNewLine(Util.readInput(11), Day11.Monkey::from).toList();
    }

    private List<Day11.Monkey> parseTestInput() {
        return Util.splitByDoubleNewLine(testInput, Day11.Monkey::from).toList();
    }

    @Test
    void testTransformation() {
        final var parsedTestInput = parseTestInput();
        Util.printfln("Test input contains: %d entries", parsedTestInput.size());
        assertThat(parsedTestInput).isNotEmpty().hasSize(4);

        final var input = readInput();
        Util.printfln("Input contains: %d entries", input.size());
        assertThat(input).isNotEmpty().hasSize(8);
    }

    @Test
    void part1() {
        assertThat(day.part1(parseTestInput())).isEqualTo(10_605L);

        assertThat(day.part1(readInput())).isEqualTo(90_882L);
    }
    @Test
    void part2() {
        assertThat(day.part2(parseTestInput())).isEqualTo(2_713_310_158L);

        assertThat(day.part2(readInput())).isEqualTo(30_893_109_657L);
    }
}