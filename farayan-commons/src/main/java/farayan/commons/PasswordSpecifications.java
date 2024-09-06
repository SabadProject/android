package farayan.commons;

public class PasswordSpecifications {
	public final int MinLength;
	public final int MaxLength;
	public final int MinLettersCount;
	public final int MinUpperLettersCount;
	public final int MinLowerLettersCount;
	public final int MinNumbersCount;
	public final int MinPunctuationsCount;
	public final String AllowedChars;

	public PasswordSpecifications(int minLength, int maxLength, int minLettersCount, int minUpperLettersCount, int minLowerLettersCount, int minNumbersCount, int minPunctuationsCount, String allowedChars) {
		MinLength = minLength;
		MaxLength = maxLength;
		MinLettersCount = minLettersCount;
		MinUpperLettersCount = minUpperLettersCount;
		MinLowerLettersCount = minLowerLettersCount;
		MinNumbersCount = minNumbersCount;
		MinPunctuationsCount = minPunctuationsCount;
		AllowedChars = allowedChars;
	}
}
