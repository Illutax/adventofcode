import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Day4
{
	public static void main(String[] args) throws IOException
	{
		File file = new File("Day2 -java/input4.txt");
		q2(file);
	}

	private static void q2(File file) throws IOException
	{
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {

			int validPassports = 0;
			Map<String, String> m = new HashMap<>();
			for (String line; (line = br.readLine()) != null; ) {
				if (line.isEmpty()) {
					m.remove("cid");
					validPassports += isPasswordValid(m);

					m = new HashMap<>();
					continue;
				}
				System.out.println(line);
				for (String kv : line.split(" ")) {
					String[] s = kv.split(":");
					m.put(s[0], s[1]);
				}
			}
			System.out.println(validPassports);
		}
	}

	private static int isPasswordValid(Map<String, String> map)
	{
		if (!sizeValidation(map)) return 0;
		if (notInrange(Integer.parseInt(map.get("byr")), 1920, 2002)) return 0;
		if (notInrange(Integer.parseInt(map.get("iyr")), 2010, 2020)) return 0;
		if (notInrange(Integer.parseInt(map.get("eyr")), 2020, 2030)) return 0;
		if (!map.get("hgt").matches("^\\d{3}cm$") && !map.get("hgt").matches("^\\d{2}in$")) return 0;
		if (map.get("hgt").matches("^\\d{3}cm$") && notInrange(Integer.parseInt(map.get("hgt").substring(0,3)), 150, 193)) return 0;
		if (map.get("hgt").matches("^\\d{2}in$") && notInrange(Integer.parseInt(map.get("hgt").substring(0,2)), 59, 76)) return 0;
		if (!map.get("hcl").matches("^#[0-9a-f]{6}$")) return 0;
		if (!map.get("ecl").matches("^amb|blu|brn|gry|grn|hzl|oth$")) return 0;
		if (!map.get("pid").matches("^\\d{9}$")) return 0;
		return 1;
	}

	private static boolean notInrange(int v, int l, int h) {
		return l > v || v > h;
	}

	private static void q1(File file) throws IOException
	{
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {

			int validPassports = 0;
			Map<String, String> m = new HashMap<>();
			for (String line; (line = br.readLine()) != null; ) {
				if (line.isEmpty()) {
					validPassports += sizeValidation(m) ? 1 : 0;

					m = new HashMap<>();
					continue;
				}
				System.out.println(line);
				for (String kv : line.split(" ")) {
					String[] s = kv.split(":");
					m.put(s[0], s[1]);
				}
			}
			System.out.println(validPassports);
		}
	}

	private static boolean sizeValidation(Map<String, String> m)
	{
		m.remove("cid");
		return m.size() == 7;
	}
}
