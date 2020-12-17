import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.BinaryOperator;

public class Day6 {

    public static void main(String[] args) {
        q1();
        q2();
    }

    private static void q2() {
        final Scanner scanner = getScanner();
        int c = 0;
        while (scanner.hasNext()) {
            String digest = Arrays.stream(scanner.next().split("\n"))
                    .filter(s -> !s.isEmpty())
                    .reduce(getLongestCommonSubSequence()).orElseThrow();
            c += digest.isEmpty() ? 0 : Arrays.stream(digest.split(""))
                    .distinct()
                    .count();
        }
        System.out.println(c);
        assert c < 3477 : "first try";
        assert c < 3450 : "second try";
        assert c == 3398 : "correct";
    }

    private static BinaryOperator<String> getLongestCommonSubSequence() {
        return (a, b) -> a.replaceAll("[^" + b + "]", "");
    }

    private static void q1() {
        final Scanner scanner = getScanner();
        int c = 0;
        while (scanner.hasNext()) {
            c += Arrays.stream(scanner.next()
                    .replaceAll("\n", "")
                    .split(""))
                    .distinct()
                    .count();
        }
        System.out.println(c);
        assert c == 6947 : "correct";
    }

    private static Scanner getScanner() {
        final File file = new File("java/input6.txt");
        assert file.exists();
        try {
            final var scanner = new Scanner(file);
            scanner.useDelimiter("\n\n");
            return scanner;
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
