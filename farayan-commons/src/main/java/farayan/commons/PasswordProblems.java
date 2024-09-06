package farayan.commons;

import android.content.Context;

public enum PasswordProblems {
	Ok(R.string.PasswordProblemsOk),
	NotEnoughLength(R.string.PasswordProblemsNotEnoughLength),
	LengthExceed(R.string.PasswordProblemsLengthExceed),
	NotEnoughLetters(R.string.PasswordProblemsNotEnoughLetters),
	NotEnoughUppercaseLetters(R.string.PasswordProblemsNotEnoughUppercaseLetters),
	NotEnoughLowercaseLetters(R.string.PasswordProblemsNotEnoughLowercaseLetters),
	NotEnoughNumbers(R.string.PasswordProblemsNotEnoughNumbers),
	NotEnoughPunctuations(R.string.PasswordProblemsNotEnoughPunctuations),
	DisallowedChars(R.string.PasswordProblemsDisallowedChars);

	public final String Message;
	public final int MessageID;

	PasswordProblems(String message) {
		this.Message = message;
		this.MessageID = 0;
	}

	PasswordProblems(int messageID) {
		this.Message = "";
		this.MessageID = messageID;
	}

	public String FormatMessage(Context ctx, PasswordSpecifications specification, boolean localizeNumbers) {
		String message = FormatMessage(ctx, specification);
		return localizeNumbers ? FarayanUtility.ConvertNumbersToPersian(message) : message;
	}

	private String FormatMessage(Context ctx, PasswordSpecifications specification) {
		String messageText = getMessageText(ctx);
		if (messageText == null)
			throw new RuntimeException("");
		switch (this) {
			case DisallowedChars:
				return messageText;

			case LengthExceed:
				return String.format(messageText, specification.MaxLength);

			case NotEnoughLength:
				return String.format(messageText, specification.MinLength);

			case NotEnoughLetters:
				return String.format(messageText, specification.MinLettersCount);

			case NotEnoughLowercaseLetters:
				return String.format(messageText, specification.MinLowerLettersCount);

			case NotEnoughNumbers:
				return String.format(messageText, specification.MinNumbersCount);

			case NotEnoughPunctuations:
				return String.format(messageText, specification.MinPunctuationsCount);

			case NotEnoughUppercaseLetters:
				return String.format(messageText, specification.MinUpperLettersCount);

			case Ok:
				return messageText;
			default:
				throw new RuntimeException("");
		}
	}

	private String getMessageText(Context ctx) {
		if (FarayanUtility.IsUsable(Message))
			return Message;
		if (MessageID != 0)
			return ctx.getString(MessageID);
		return null;
	}
}
