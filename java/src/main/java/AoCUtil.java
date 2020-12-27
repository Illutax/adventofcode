import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


public class AoCUtil {
    @SneakyThrows
    @NotNull
    public static List<Long> parseInputToLongList(
                    int day) {
        return Files.lines(Paths.get("input" + day + ".txt"))
                        .map(Long::parseLong).collect(Collectors.toList());
    }

    @SneakyThrows
    @NotNull
    public static List<Integer> parseInputToIntList(
                    int day) {
        return Files.lines(Paths.get("input" + day + ".txt"))
                        .map(Integer::parseInt).collect(Collectors.toList());
    }

    @SneakyThrows
    @NotNull
    public static List<String> parseInputToStringList(
                    int day) {
        return Files.lines(Paths.get("input" + day + ".txt"))
                        .collect(Collectors.toList());
    }

    @SneakyThrows
    @NotNull
    public static int[][] parseGrid(int day,
                                                  Function<String, Integer> parseRules) {
        List<int[]> grid = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                        new FileReader(new File("input" + day + ".txt")))) {
            for (String line; (line = br.readLine()) != null; ) {
                grid.add(Arrays.stream(line.split(""))
                                .mapToInt(parseRules::apply).toArray());
            }

            //System.out.println("Amount of rows: " + grid.size());
            //for (int[] r : grid)
            //{
            //    for (int c : r)
            //    {
            //        System.out.print(c);
            //    }
            //    System.out.print("\n");
            //}
        }
        return grid.toArray(new int[0][]);
    }
}
