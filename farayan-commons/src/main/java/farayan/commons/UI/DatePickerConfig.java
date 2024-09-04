package farayan.commons.UI;

import android.graphics.Typeface;

import farayan.commons.UI.Commons.DatePickerTimeModes;
import farayan.commons.UI.Commons.HourModes;
import farayan.commons.UI.Commons.NumberLanguages;
import farayan.commons.UI.Commons.TextStyle;

public class DatePickerConfig {

	DatePickerTimeModes datePickerTimeMode;

	/**
	 * تنظیمات انتخاب‌گر تاریخ و ساعت
	 */
	public Integer SpecifiedMinYear;
	public Integer SpecifiedMaxYear;
	/**
	 * مقدار تاریخ پیش‌فرض
	 */
	public int SelectedYear;
	public int SelectedMonthFrom1;
	public int SelectedDay;
	public int SelectedHour;
	public int SelectedMinute;
	public int SelectedSecond;

	/**
	 * رنگ‌ها
	 */
	public int DefaultColor;
	public int YearColor;
	public int MonthColor;
	public int DayNumberColor;
	public int DayNameColor;
	public int HourColor;
	// TODO: 17/08/2017 add this to attributes
	public int HourMinuteSeparatorColor;
	public int MinuteColor;
	// TODO: 17/08/2017 add this to attributes
	public int MinuteSecondSeparatorColor;
	public int SecondColor;
	// TODO: 17/08/2017 add this to attributes
	public int PreparedItemsColor;
	/**
	 *
	 */
	public boolean LeadingZero;
	public boolean DayNameDisplay;
	public boolean SecondDisplay;
	public int DateTimeMode;
	public DateTimePickerComponent.MonthDisplayModes MonthDisplayMode;
	public String MonthDisplayTemplate;
	public Typeface defaultTypeface;
	public boolean DayNumberLeadingZero;
	public boolean HourNumberLeadingZero;
	public boolean MinuteNumberLeadingZero;
	public boolean SecondNumberLeadingZero;
	public int MinuteStep = 1;
	public int SecondStep = 1;
	public int HourStep = 1;
	public HourModes HourMode = HourModes.TwentyFour24Hours;
	public NumberLanguages NumberLanguage;

	public int DefaultTextSize = 40;
	public int YearTextSize = DefaultTextSize;
	public int MonthTextSize= DefaultTextSize;
	public int DayNumberTextSize= DefaultTextSize;
	public int DayNameTextSize= DefaultTextSize;
	public int HourTextSize= DefaultTextSize;
	public int HourMinuteSeparatorTextSize= DefaultTextSize;
	public int MinuteTextSize= DefaultTextSize;
	public int MinuteSecondSeparatorTextSize= DefaultTextSize;
	public int SecondTextSize= DefaultTextSize;
	public int PreparedItemsTextSize= DefaultTextSize;

	public final TextStyle DateHeaderTextStyle = new TextStyle();
	public final TextStyle TimeHeaderTextStyle = new TextStyle();
	public final TextStyle PreparedHeaderTextStyle = new TextStyle();


	public int getHourStep() {
		if (HourStep < 1)
			HourStep = 1;
		return HourStep;
	}

	public int getMinuteStep() {
		if (MinuteStep < 1)
			MinuteStep = 1;
		return MinuteStep;
	}

	public int getSecondStep() {
		if (SecondStep < 1)
			SecondStep = 1;
		return SecondStep;
	}
}
