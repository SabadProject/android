package farayan.commons;

import farayan.commons.Commons.DateTimeParts;

public class PersianPeriod
{

	PersianCalendar start;
	PersianCalendar finish;

	public PersianCalendar getStart() {
		return start;
	}

	public PersianCalendar getFinish() {
		return finish;
	}

	private boolean isRevered;

	public boolean getIsRevered() {
		return isRevered;
	}

	public PersianPeriod(long milliseconds) {
		this.start = new PersianCalendar();
		this.finish = (PersianCalendar) new PersianCalendar().addMilliseconds(milliseconds);
	}

	public PersianPeriod(long start, long finish) {
		this(new PersianCalendar(start), new PersianCalendar(finish));
	}

	public PersianPeriod(PersianCalendar start, PersianCalendar finish) {
		if (start != null && finish != null)
			isRevered = start.after(finish);
		this.start = isRevered ? finish : start;
		this.finish = isRevered ? start : finish;
	}

	public float TotalYears() {
		return Years() + ((float) Months() / (float) 12);
	}

	public int TotalMonths() {
		if (TotalYears() == 0) {
			return finish.getPersianMonthIndexFrom1() - start.getPersianMonthIndexFrom1();
		} else {
			return Years() * 12 + Months();// (12 - start.getPersianMonthIndexFrom1()) + (Years() * 12) - finish.getPersianMonthIndexFrom1();
		}
	}

	public int TotalWeeks() {
		return TotalDays() / 7;
	}

	public int TotalDays() {
		if (start.getPersianYear() == finish.getPersianYear()) {
			return GetPassedDays(finish) - GetPassedDays(start);
		} else {
			int total = GetRemainedDays(start);
			for (int i = start.getPersianYear() + 1; i < finish.getPersianYear(); i++) {
				total += PersianCalendarUtils.isPersianLeapYear(i) ? 366 : 365;
			}
			total += GetPassedDays(finish);
			return total;
		}
	}

	private int GetRemainedDays(PersianCalendar date) {
		int yearDaysCount = date.IsPersianLeapYear() ? 366 : 365;
		return yearDaysCount - GetPassedDays(date);
	}

	private int GetPassedDays(PersianCalendar date) {
		int total = 0;
		for (int i = 0; i < date.getPersianMonthIndexFrom1() - 1; i++) {
			if (i < 6)
				total += 31;
			if (i >= 6 && i < 11)
				total += 30;
		}
		total += date.getPersianDay();
		return total;
	}

	public int TotalHours() {
		int totalDays = TotalDays();
		if (totalDays == 0)
			return Hours();// finish.getHour() - start.getHour();
		return (24 - start.getHour()) + ((totalDays - 1) * 24) + finish.getHour();
	}

	public int TotalMinutes() {
		int totalHours = TotalHours();
		if (totalHours == 0)
			return finish.getMinute() - start.getMinute();
		return (60 - start.getMinute()) + ((totalHours - 1) * 60) + finish.getMinute();
	}

	public int TotalSeconds() {
		int totalMinutes = TotalMinutes();
		if (totalMinutes == 0)
			return finish.getSecond() - start.getSecond();
		return (60 - start.getSecond()) + (totalMinutes - 1) * 60 + finish.getSecond();
	}

	public int Years() {
		return finish.getPersianYear() - start.getPersianYear() - (Decrease1Year() ? 1 : 0);
	}

	private boolean Decrease1Year() {
		int startValue = start.getPersianMonthIndexFrom1();
		int finishValue = finish.getPersianMonthIndexFrom1();
		return (finishValue < startValue) || (finishValue == startValue && Decrease1Month());
	}

	public int Months() {
		int diff = finish.getPersianMonthIndexFrom1() - start.getPersianMonthIndexFrom1() - (Decrease1Month() ? 1 : 0);
		return (diff >= 0 ? diff : 12 + diff);
	}

	private boolean Decrease1Month() {
		int startValue = start.getPersianDay();
		int finishValue = finish.getPersianDay();
		return (finishValue < startValue) || (finishValue == startValue && Decrease1Day());
	}

	public int Days() {
		int diff = finish.getPersianDay() - start.getPersianDay() - (Decrease1Day() ? 1 : 0);
		if (diff >= 0) {
			return diff;
		}
		int previousMonth = finish.getPersianMonthIndexFrom1() - 1;
		if (previousMonth == 0)
			previousMonth = 12;
		int previousMonthDaysCount = 0;
		if (previousMonth >= 1 && previousMonth <= 6)
			previousMonthDaysCount = 31;
		if (previousMonth > 6 && previousMonth < 12)
			previousMonthDaysCount = 30;
		if (previousMonth == 12) {
			previousMonthDaysCount = PersianCalendarUtils.isPersianLeapYear(finish.getPersianYear() - 1) ? 30 : 29;
		}
		return previousMonthDaysCount - start.getPersianDay() + finish.getPersianDay() - (Decrease1Day() ? 1 : 0);
	}

	private boolean Decrease1Day() {
		int startValue = start.getHour();
		int finishValue = finish.getHour();
		return (finishValue < startValue) || (finishValue == startValue && Decrease1Hour());
	}

	public int Hours() {
		int diff = finish.getHour() - start.getHour() - (Decrease1Hour() ? 1 : 0);
		return (diff >= 0 ? diff : 24 + diff);
	}

	private boolean Decrease1Hour() {
		int startValue = start.getMinute();
		int finishValue = finish.getMinute();
		return (finishValue < startValue) || (finishValue == startValue && Decrease1Minute());
	}

	public int Minutes() {
		int diff = finish.getMinute() - start.getMinute() - (Decrease1Minute() ? 1 : 0);
		return (diff >= 0 ? diff : 60 + diff);
	}

	private boolean Decrease1Minute() {
		return finish.getSecond() < start.getSecond();
	}

	public int Seconds() {
		int diff = finish.getSecond() - start.getSecond();
		return diff >= 0 ? diff : 60 + diff;
	}

	public PersianCalendar ToSeconds(int seconds) {
		int currentSeconds = TotalSeconds();
		int days = (seconds - currentSeconds) / 86400;
		start.addDays(days);
		return start;
	}

	public String toString(boolean showZero) {
		String result = "";
		if (showZero || this.Years() > 0)
			result += this.Years() + " سال و ";
		if (showZero || this.Months() > 0)
			result += this.Months() + " ماه و ";
		if (showZero || this.Days() > 0)
			result += this.Days() + " روز و ";
		if (showZero || this.Hours() > 0)
			result += this.Hours() + " ساعت و ";
		if (showZero || this.Minutes() > 0)
			result += this.Minutes() + " دقیقه و ";
		if (showZero || this.Seconds() > 0)
			result += this.Seconds() + " ثانیه ";
		if (result.endsWith(" و "))
			result = result.substring(0, result.lastIndexOf(" و "));
		return result;
	}

	public String toString(boolean includeZeros, boolean persianNumbers, DateTimeParts leftEdgeType) {
		String result = "";

		switch (leftEdgeType) {
			case Year:
				if (includeZeros || this.Years() > 0)
					result += FarayanUtility.MoneyFormatted(this.Years(), persianNumbers) + " سال و ";
				break;
			case Month:
				if (includeZeros || this.TotalMonths() > 0)
					result += FarayanUtility.MoneyFormatted(this.TotalMonths(), persianNumbers) + " ماه و ";
				break;
			case Day:
				if (includeZeros || this.TotalDays() > 0)
					result += FarayanUtility.MoneyFormatted(this.TotalDays(), persianNumbers) + " روز و ";
				break;
			case Hour:
				if (includeZeros || this.TotalHours() > 0)
					result += FarayanUtility.MoneyFormatted(this.TotalHours(), persianNumbers) + " ساعت و ";
				break;
			case Minute:
				if (includeZeros || this.TotalMinutes() > 0)
					result += FarayanUtility.MoneyFormatted(this.TotalMinutes(), persianNumbers) + " دقیقه و ";
				break;
			case Second:
				if (includeZeros || this.TotalSeconds() > 0)
					result += FarayanUtility.MoneyFormatted(this.TotalSeconds(), persianNumbers) + " ثانیه و ";
				break;

		}

		if (leftEdgeType.Order < DateTimeParts.Year.Order && (includeZeros || this.Years() > 0))
			result += FarayanUtility.MoneyFormatted(this.Years(), persianNumbers) + " سال و ";

		if (leftEdgeType.Order < DateTimeParts.Month.Order && (includeZeros || this.Months() > 0))
			result += FarayanUtility.MoneyFormatted(this.Months(), persianNumbers) + " ماه و ";

		if (leftEdgeType.Order < DateTimeParts.Day.Order && (includeZeros || this.Days() > 0))
			result += FarayanUtility.MoneyFormatted(this.Days(), persianNumbers) + " روز و ";

		if (leftEdgeType.Order < DateTimeParts.Hour.Order && (includeZeros || this.Hours() > 0))
			result += FarayanUtility.MoneyFormatted(this.Hours(), persianNumbers) + " ساعت و ";

		if (leftEdgeType.Order < DateTimeParts.Minute.Order && (includeZeros || this.Minutes() > 0))
			result += FarayanUtility.MoneyFormatted(this.Minutes(), persianNumbers) + " دقیقه و ";

		if (leftEdgeType.Order < DateTimeParts.Second.Order && (includeZeros || this.Seconds() > 0))
			result += FarayanUtility.MoneyFormatted(this.Seconds(), persianNumbers) + " ثانیه ";

		if (result.endsWith(" و "))
			result = result.substring(0, result.lastIndexOf(" و "));
		return result;
	}

	public String Standard(boolean includeZeros, boolean persianNumbers, DateTimeParts leftEdgeType) {
		String result = "";

		switch (leftEdgeType) {
			case Year:
				if (includeZeros || this.Years() > 0)
					result += FarayanUtility.MoneyFormatted(this.Years(), persianNumbers, false) + "/";
				break;
			case Month:
				if (includeZeros || this.TotalMonths() > 0)
					result += FarayanUtility.MoneyFormatted(this.TotalMonths(), persianNumbers, false) + "/";
				break;
			case Day:
				if (includeZeros || this.TotalDays() > 0)
					result += FarayanUtility.MoneyFormatted(this.TotalDays(), persianNumbers, false) + ".";
				break;
			case Hour:
				if (includeZeros || this.TotalHours() > 0)
					result += FarayanUtility.MoneyFormatted(this.TotalHours(), persianNumbers, false) + ":";
				break;
			case Minute:
				if (includeZeros || this.TotalMinutes() > 0)
					result += FarayanUtility.MoneyFormatted(this.TotalMinutes(), persianNumbers, false) + ":";
				break;
			case Second:
				if (includeZeros || this.TotalSeconds() > 0)
					result += FarayanUtility.MoneyFormatted(this.TotalSeconds(), persianNumbers, false) + ":";
				break;

		}

		if (leftEdgeType.Order < DateTimeParts.Year.Order && (includeZeros || this.Years() > 0))
			result += FarayanUtility.MoneyFormatted(this.Years(), persianNumbers, false) + "/";

		if (leftEdgeType.Order < DateTimeParts.Month.Order && (includeZeros || this.Months() > 0))
			result += FarayanUtility.MoneyFormatted(this.Months(), persianNumbers, false) + "/";

		if (leftEdgeType.Order < DateTimeParts.Day.Order && (includeZeros || this.Days() > 0))
			result += FarayanUtility.MoneyFormatted(this.Days(), persianNumbers, false) + ".";

		if (leftEdgeType.Order < DateTimeParts.Hour.Order && (includeZeros || this.Hours() > 0))
			result += FarayanUtility.MoneyFormatted(this.Hours(), persianNumbers, false) + ":";

		if (leftEdgeType.Order < DateTimeParts.Minute.Order && (includeZeros || this.Minutes() > 0))
			result += FarayanUtility.MoneyFormatted(this.Minutes(), persianNumbers, false) + ":";

		if (leftEdgeType.Order < DateTimeParts.Second.Order && (includeZeros || this.Seconds() > 0))
			result += FarayanUtility.MoneyFormatted(this.Seconds(), persianNumbers, false) + "";

		return result;
	}

	public String EstimatedFull(boolean includeZeros, boolean persianNumbers, DateTimeParts leftEdgeType) {
		if (leftEdgeType.Order <= DateTimeParts.Year.Order && TotalYears() > 0)
			return FarayanUtility.MoneyFormatted(TotalYears(), persianNumbers, false) + " سال";

		if (leftEdgeType.Order <= DateTimeParts.Month.Order && TotalMonths() > 0)
			return FarayanUtility.MoneyFormatted(TotalMonths(), persianNumbers, false) + " ماه";

		if (leftEdgeType.Order <= DateTimeParts.Month.Order && TotalDays() > 0)
			return FarayanUtility.MoneyFormatted(TotalDays(), persianNumbers, false) + " روز";

		if (leftEdgeType.Order <= DateTimeParts.Hour.Order && TotalHours() > 0)
			return FarayanUtility.MoneyFormatted(TotalHours(), persianNumbers, false) + " ساعت";

		if (leftEdgeType.Order <= DateTimeParts.Minute.Order && TotalMinutes() > 0)
			return FarayanUtility.MoneyFormatted(TotalMinutes(), persianNumbers, false) + " دقیقه";

		if (leftEdgeType.Order <= DateTimeParts.Second.Order && TotalSeconds() > 0)
			return FarayanUtility.MoneyFormatted(TotalSeconds(), persianNumbers, false) + " ثانیه";

		return null;
	}

	public String EstimatedSummary(boolean includeZeros, boolean persianNumbers, DateTimeParts leftEdgeType) {
		if (leftEdgeType.Order <= DateTimeParts.Year.Order && TotalYears() > 0)
			return FarayanUtility.MoneyFormatted(TotalYears(), persianNumbers, false) + "س";

		if (leftEdgeType.Order <= DateTimeParts.Month.Order && TotalMonths() > 0)
			return FarayanUtility.MoneyFormatted(TotalMonths(), persianNumbers, false) + "م";

		if (leftEdgeType.Order <= DateTimeParts.Month.Order && TotalDays() > 0)
			return FarayanUtility.MoneyFormatted(TotalDays(), persianNumbers, false) + "ر";

		if (leftEdgeType.Order <= DateTimeParts.Hour.Order && TotalHours() > 0)
			return FarayanUtility.MoneyFormatted(TotalHours(), persianNumbers, false) + "س";

		if (leftEdgeType.Order <= DateTimeParts.Minute.Order && TotalMinutes() > 0)
			return FarayanUtility.MoneyFormatted(TotalMinutes(), persianNumbers, false) + "د";

		if (leftEdgeType.Order <= DateTimeParts.Second.Order && TotalSeconds() > 0)
			return FarayanUtility.MoneyFormatted(TotalSeconds(), persianNumbers, false) + "ث";

		return null;
	}
}
