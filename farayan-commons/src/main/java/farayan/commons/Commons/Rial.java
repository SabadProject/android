package farayan.commons.Commons;

import android.content.res.Resources;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import farayan.commons.Exceptions.Rexception;
import farayan.commons.FarayanUtility;
import farayan.commons.R;

public class Rial
{
	public Rial(double amount) {
		this(amount, 1);
	}

	public Rial(double amount, int precision) {
		TheAmount = amount;
		Coefficient = findCoefficient(amount, precision);
		Displayable = amount / Coefficient.Coefficient;
	}

	public Rial(double displayable, Coefficients coefficient) {
		Displayable = displayable;
		Coefficient = coefficient;
		TheAmount = Displayable * Coefficient.Coefficient;
	}

	public Rial(Coefficients coefficient, double amount) {
		TheAmount = amount;
		Coefficient = coefficient == null ? findCoefficient(amount, 1) : coefficient;
		Displayable = TheAmount / Coefficient.Coefficient;
	}

	private Coefficients findCoefficient(double amount, int precision) {
		return Arrays
				.stream(Coefficients.values())
				.sorted((x, y) -> Long.compare(x.Coefficient, y.Coefficient))
				.filter(x -> x.Coefficient <= amount)
				.filter(x -> FarayanUtility.Round(amount / x.Coefficient, precision) == (amount / x.Coefficient))
				.max((x, y) -> Long.compare(x.Coefficient, y.Coefficient))
				.orElse(Coefficients.Rial);
	}

	public enum Coefficients
	{
		Rial(1, R.string.RialCoefficientRial),
		Tooman(10, R.string.RialCoefficientTooman),
		HezarTooman(10 * 1000, R.string.RialCoefficientHezarTooman),
		MillionTooman(10 * 1000 * 1000, R.string.RialCoefficientMillionTooman),
		MillardTooman(10 * 1000 * 1000 * 1000L, R.string.RialCoefficientMillardTooman),
		;

		public final long Coefficient;
		public final int NameResID;

		Coefficients(long coefficient, int nameResID) {
			Coefficient = coefficient;
			NameResID = nameResID;
		}

		public static int Compared(Coefficients x, Coefficients y) {
			return Long.compare(x.Coefficient, y.Coefficient);
		}
	}

	public final double TheAmount;
	public final double Displayable;
	public final Coefficients Coefficient;

	public String Textual(Resources resources) {
		return String.format("%s %s", Displayable(), resources.getString(Coefficient.NameResID));
	}

	public String Displayable() {
		return FarayanUtility.MoneyFormatted(Displayable, true, false);
	}
}
