import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.IntStream;

public class Day5
{
	public static void main(String[] args)	{
		q1();
		q2();
	}

	private static void q1() {
		final int max = parseBinary().max().orElseThrow();
		System.out.println(max);
		assert max == 874;
	}

	private static IntStream parseBinary()	{
		try {
			final String path = "Day2 -java/input5.txt";
			return Files.lines(Paths.get(path))
					.map(line -> line.replaceAll("B|R", "1"))
					.map(line -> line.replaceAll("F|L", "0"))
					.mapToInt(line -> Integer.parseInt(line, 2));
		}
		catch (IOException e) {
			return null;
		}
	}

	private static void q2() {
		final int lastOccupiedSeatBeforeMine= parseBinary().sorted()
				.reduce((a,b) -> b-a == 1 ? b : a).orElseThrow();
		System.out.println(lastOccupiedSeatBeforeMine + 1);
	}
}
