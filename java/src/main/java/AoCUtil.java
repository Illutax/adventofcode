import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class AoCUtil {
    public static List<Long> parseInputToListOfLongs(int day) {
        try {
            return Files.lines(Paths.get("input" + day + ".txt"))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static List<Integer> parseInputToListOfInts(int day) {
        try {
            return Files.lines(Paths.get("input" + day + ".txt"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
