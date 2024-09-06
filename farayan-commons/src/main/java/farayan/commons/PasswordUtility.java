package farayan.commons;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PasswordUtility {

	public final static String PunctuationsText = "~!@#$%^&*()_+`-={}[];:|<>,./?";
	public final static String NumericCharsText = "0123456789";
	public final static String UpperCharsText = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public final static String LowerCharsText = "abcdefghijklmnopqrstuvwxyz";
	public final static String AlphaCharsText = UpperCharsText + LowerCharsText;
	public final static String PasswordText = PunctuationsText + NumericCharsText + AlphaCharsText;

	public final static char[] UpperCharsChars = UpperCharsText.toCharArray();
	public final static char[] LowerCharsChars = LowerCharsText.toCharArray();
	public final static char[] AlphaChars = AlphaCharsText.toCharArray();
	public final static char[] PunctuationsChars = PunctuationsText.toCharArray();
	public final static char[] NumericChars = NumericCharsText.toCharArray();
	public final static char[] PasswordChars = PasswordText.toCharArray();


	public static PasswordProblems PasswordProblem(String password) {
		return PasswordProblem(password, new PasswordSpecifications(5, 64, 1, 0, 0, 1, 0, ""));
	}
	
	public static PasswordProblems PasswordProblem(String password, PasswordSpecifications passwordSpecifications) {
		if (password.length() < passwordSpecifications.MinLength)
			return PasswordProblems.NotEnoughLength;

		if (password.length() > passwordSpecifications.MaxLength)
			return PasswordProblems.LengthExceed;

		if (CountChars(password, PasswordUtility.AlphaChars) < passwordSpecifications.MinLettersCount)
			return PasswordProblems.NotEnoughLetters;

		if (CountChars(password, PasswordUtility.LowerCharsChars) < passwordSpecifications.MinLowerLettersCount)
			return PasswordProblems.NotEnoughLowercaseLetters;

		if (CountChars(password, PasswordUtility.UpperCharsChars) < passwordSpecifications.MinUpperLettersCount)
			return PasswordProblems.NotEnoughUppercaseLetters;

		if (CountChars(password, PasswordUtility.NumericChars) < passwordSpecifications.MinNumbersCount)
			return PasswordProblems.NotEnoughNumbers;

		if (CountChars(password, PasswordUtility.PunctuationsChars) < passwordSpecifications.MinPunctuationsCount)
			return PasswordProblems.NotEnoughPunctuations;

		if (ContainsOtherChars(password, (PasswordUtility.PasswordText + passwordSpecifications.AllowedChars).toCharArray()))
			return PasswordProblems.DisallowedChars;

		return PasswordProblems.Ok;
	}

	public static boolean ContainsOtherChars(String password, char[] chars) {
		for (char c1 : password.toCharArray()) {
			boolean found = false;
			for (char c2 : chars) {
				if (c1 == c2) {
					found = true;
				}
			}
			if (!found)
				return true;
		}

		return false;
	}

	public static boolean ContainsChar(String password, char[] chars) {
		for (char c1 : password.toCharArray()) {
			for (char c2 : chars) {
				if (c1 == c2) {
					return true;
				}
			}
		}

		return false;
	}

	public static int CountChars(String password, char[] chars) {
		int count = 0;
		for (char c1 : password.toCharArray()) {
			for (char c2 : chars) {
				if (c1 == c2) {
					count++;
				}
			}
		}

		return count;
	}

	public static String NewPassword(int alphasCount, int numbersCount, int punctuationsCount, PasswordCaseModes mode) {
		HashMap<Integer, String> password = new HashMap<Integer, String>();
		for (int i = 0; i < alphasCount; i++) {
			if (mode == PasswordCaseModes.Both) {
				int rand = FarayanUtility.NextRandom(0, 2);
				switch (rand) {
					case 0:
						password.put(FarayanUtility.NextRandom(0, 10000), UpperCharsChars[FarayanUtility.NextRandom(0, UpperCharsChars.length)] + "");
						break;
					case 1:
						password.put(FarayanUtility.NextRandom(0, 10000), LowerCharsChars[FarayanUtility.NextRandom(0, LowerCharsChars.length)] + "");
						break;
					default:
						break;
				}
			} else {
				password.put(FarayanUtility.NextRandom(0, 10000), AlphaChars[FarayanUtility.NextRandom(0, AlphaChars.length)] + "");
			}
		}

		for (int i = 0; i < numbersCount; i++)
			password.put(FarayanUtility.NextRandom(0, 10000), NumericChars[FarayanUtility.NextRandom(0, NumericChars.length)] + "");

		for (int i = 0; i < punctuationsCount; i++)
			password.put(FarayanUtility.NextRandom(0, 10000), PunctuationsChars[FarayanUtility.NextRandom(0, PunctuationsChars.length)] + "");

		List<Integer> keys = new ArrayList<Integer>();
		for (int d : password.keySet())
			keys.add(d);
		Collections.sort(keys);

		String result = "";

		for (Integer key : keys)
			result += password.get(key);

		if (mode == PasswordCaseModes.Samecase) {
			int rand = FarayanUtility.NextRandom(0, 2);
			switch (rand) {
				case 0:
					mode = PasswordCaseModes.Lowercase;
					break;
				case 1:
					mode = PasswordCaseModes.Uppercase;
					break;
				default:
					break;
			}
		} else if (mode == PasswordCaseModes.Both) {
			boolean containsUppercase = ContainsChar(result, UpperCharsChars);
			boolean containsLowercase = ContainsChar(result, LowerCharsChars);
			if (containsLowercase != containsUppercase) {
				if (!containsLowercase) {
					result += UpperCharsChars[FarayanUtility.NextRandom(0, LowerCharsChars.length)];
				}
				if (!containsLowercase) {
					result += UpperCharsChars[FarayanUtility.NextRandom(0, UpperCharsChars.length)];
				}
			}
		}

		switch (mode) {
			case Lowercase:
				result = result.toLowerCase();
				break;
			case Uppercase:
				result = result.toUpperCase();
				break;
			default:
				break;
		}

		return result;
	}
}
