import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

public class Day2
{

	private static File input;
	public static void main(String[] args)
	{
		final Day2 day2 = new Day2();
		input = new File("input.txt");
		day2.q1();
		day2.q2();
	}

	private static class PasswordPolicyDao
	{
		public String password, letterOfInterest;
		public int min, max;
	}

	private void q1() {
		Function<PasswordPolicyDao, Boolean> passwordPolicy = passwordPolicyDao -> {
			int amountOfLetterOccurrences = passwordPolicyDao.password.length() - passwordPolicyDao.password.replace(passwordPolicyDao.letterOfInterest, "").length();
			return passwordPolicyDao.min <= amountOfLetterOccurrences && amountOfLetterOccurrences <= passwordPolicyDao.max;
		};
		final int amountOfCorrectPassword = determineAmountOfCorrectPassword(input, passwordPolicy);
		System.out.println(amountOfCorrectPassword);
	}

	private void q2() {
		Function<PasswordPolicyDao, Boolean> passwordPolicy = passwordPolicyDao -> {
			final boolean firstLetterMatched = passwordPolicyDao.password.charAt(passwordPolicyDao.min - 1) == passwordPolicyDao.letterOfInterest.charAt(0);
			final boolean secondLetterMatched = passwordPolicyDao.password.charAt(passwordPolicyDao.max - 1) == passwordPolicyDao.letterOfInterest.charAt(0);
			return firstLetterMatched ^ secondLetterMatched;
		};
		final int amountOfCorrectPassword = determineAmountOfCorrectPassword(input, passwordPolicy);
		System.out.println(amountOfCorrectPassword);
	}

	private int determineAmountOfCorrectPassword(File input, Function<PasswordPolicyDao, Boolean> isPasswordPolicyConform)
	{
		int amountOfCorrectPasswords = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(input)))
		{
			String line;
			while((line = br.readLine()) != null)
			{
				String[] parts = line.split(" ");
				String[] range = parts[0].split("-");
				PasswordPolicyDao passwordPolicyDao = new PasswordPolicyDao();
				passwordPolicyDao.password = parts[2];
				passwordPolicyDao.letterOfInterest = parts[1].substring(0, 1);
				passwordPolicyDao.min = Integer.parseInt(range[0]);
				passwordPolicyDao.max = Integer.parseInt(range[1]);
				if (isPasswordPolicyConform.apply(passwordPolicyDao))
					amountOfCorrectPasswords++;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return amountOfCorrectPasswords;
	}


	/*private void q1_var() {
		long count = Arrays.stream(input.split("\n")).map(line -> {
			String[] s = line.split(" ");
			Integer[] s2 = Arrays.stream(s[0].split("-")).map(Integer::parseInt).toArray(Integer[]::new);
			int min = s2[0];
			int max = s2[1];
			long c = s[2].chars().mapToObj(i -> (char) i).filter(l -> s[1].charAt(0) == l).count();
			return max >= c && c >= min;
		}).filter(Boolean.TRUE::equals).count();
		System.out.println(count);
	}

	private void q2_var() {
		String[] lines = input.split("\n");
		int amountOfCorrectPasswords = 0;
		for (String line : lines)
		{
			String[] parts = line.split(" ");
			String password = parts[2];
			char letterOfInterest = parts[1].charAt(0);
			String[] range = parts[0].split("-");
			int min = Integer.parseInt(range[0]);
			int max = Integer.parseInt(range[1]);
			final boolean firstLetterMatched = password.charAt(min - 1) == letterOfInterest;
			final boolean secondLetterMatched = password.charAt(max - 1) == letterOfInterest;
			final boolean condition = firstLetterMatched ^ secondLetterMatched;
			if (condition)
				amountOfCorrectPasswords++;
		}

		System.out.println(amountOfCorrectPasswords);
	}*/
}
