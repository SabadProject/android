package farayan.commons.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.github.florent37.singledateandtimepicker.widget.WheelPicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import androidx.core.content.ContextCompat;
import farayan.commons.FarayanUtility;
import farayan.commons.PersianCalendar;
import farayan.commons.PersianCalendarUtils;
import farayan.commons.PersianDateTime;
import farayan.commons.PersianMonths;
import farayan.commons.R;
import farayan.commons.UI.Commons.DatePickerTimeModes;
import farayan.commons.UI.Commons.HourModes;
import farayan.commons.UI.Commons.NumberLanguages;
import farayan.commons.UI.Commons.PreparedDates;
import farayan.commons.UI.Commons.TextStyle;
import farayan.commons.UI.Core.IGeneralEvent;
import farayan.commons.UI.Core.IGeneralEventProvider;
import farayan.commons.WeekDays;

import static android.R.color.black;

public class DateTimePickerComponent extends DateTimePickerComponentParent1 implements IGeneralEventProvider<Void, DateTimePickerComponent.Events, PersianDateTime> {

	private IGeneralEvent<Void, Events, PersianDateTime> TheEvent;
	private DatePickerConfig datePickerConfig;
	public static final String BeforeNoon = "ق‌ظ";
	public static final String AfterNoon = "ب‌ظ";

	@Override
	public void SetEventHandler(IGeneralEvent<Void, Events, PersianDateTime> iEvent) {
		TheEvent = iEvent;
	}

	public enum Events {
		Changed
	}

	enum DateTimeModes {
		Date(1),
		Time(2),
		Prepared(4),;
		public static int All = DateTimeModes.Date.Code | DateTimeModes.Time.Code | DateTimeModes.Prepared.Code;
		public final int Code;

		DateTimeModes(int code) {
			Code = code;
		}

		public boolean Contained(int dateTimeMode) {
			return (Code & dateTimeMode) == Code;
		}

		public static DateTimeModes ByCode(int code) {
			for (DateTimeModes dateTimeMode : values()) {
				if (dateTimeMode.Code == code)
					return dateTimeMode;
			}
			return null;
		}
	}

	enum MonthDisplayModes {
		ByName(0),
		ByNumber(1),
		ByTemplate(2),;

		public final int Code;

		MonthDisplayModes(int code) {
			Code = code;
		}

		public static MonthDisplayModes ByCode(int code) {
			for (MonthDisplayModes item : values()) {
				if (item.Code == code)
					return item;
			}
			return null;
		}
	}

	private int minYear;
	private int maxYear;

	private void dateChanged() {
		int year = minYear + yearNumberPicker().getCurrentItemPosition();
		boolean isLeapYear = PersianCalendarUtils.isPersianLeapYear(year);

		int month = monthNumberPicker().getCurrentItemPosition() + 1;
		int day = dayNumberPicker().getCurrentItemPosition() + 1;

		if (month < 7) {
			dayNumberPicker().setRangeValues(1, 31, datePickerConfig.DayNumberLeadingZero, datePickerConfig.NumberLanguage);
		} else if (month > 6 && month < 12) {
			if (day == 31) {
				dayNumberPicker().setValue(30);
			}
			dayNumberPicker().setRangeValues(1, 30, datePickerConfig.DayNumberLeadingZero, datePickerConfig.NumberLanguage);
		} else if (month == 12) {
			if (isLeapYear) {
				if (day == 31) {
					dayNumberPicker().setValue(30);
				}
				dayNumberPicker().setRangeValues(1, 30, datePickerConfig.DayNumberLeadingZero, datePickerConfig.NumberLanguage);
			} else {
				if (day > 29) {
					dayNumberPicker().setValue(29);
				}
				dayNumberPicker().setRangeValues(1, 29, datePickerConfig.DayNumberLeadingZero, datePickerConfig.NumberLanguage);
			}
		}
		PersianDateTime persianDateTime = new PersianCalendar(
				year,
				month,
				day,
				(Integer) hourNumberPicker().getValue(),
				(Integer) minuteNumberPicker().getValue(),
				(Integer) secondNumberPicker().getValue(),
				0,
				null
		);
		dayNamePicker().scrollTo(persianDateTime.PersianWeekDay().PersianDayIndex - 1);
		if (TheEvent != null)
			TheEvent.OnEvent(Events.Changed, persianDateTime);
	}

	public DateTimePickerComponent(Context context) {
		super(context);
	}

	public DateTimePickerComponent(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DateTimePickerComponent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public DateTimePickerComponent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}


	//==================


	@Override
	protected void InitializeLayout(@Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		PersianCalendar now = new PersianCalendar();
		now.setTimeZone(TimeZone.getDefault());

		TypedArray attributes = Ctx.obtainStyledAttributes(attrs, R.styleable.DateTimePickerComponent, defStyleAttr, defStyleRes);

		datePickerConfig = new DatePickerConfig();

		///////////////////
		/**
		 * Min and Max
		 */
		datePickerConfig.SpecifiedMinYear = attributes.getInt(R.styleable.DateTimePickerComponent_minYear, 1300);
		datePickerConfig.SpecifiedMaxYear = attributes.getInt(R.styleable.DateTimePickerComponent_maxYear, 1999);

		/**
		 * Colors
		 */
		datePickerConfig.DefaultColor = attributes.getColor(R.styleable.DateTimePickerComponent_defaultColor, ContextCompat.getColor(getContext(), black));
		datePickerConfig.YearColor = attributes.getColor(R.styleable.DateTimePickerComponent_yearColor, datePickerConfig.DefaultColor);
		datePickerConfig.MonthColor = attributes.getColor(R.styleable.DateTimePickerComponent_monthColor, datePickerConfig.DefaultColor);
		datePickerConfig.DayNumberColor = attributes.getColor(R.styleable.DateTimePickerComponent_dayNumberColor, datePickerConfig.DefaultColor);
		datePickerConfig.DayNameColor = attributes.getColor(R.styleable.DateTimePickerComponent_dayNameColor, datePickerConfig.DefaultColor);
		datePickerConfig.HourColor = attributes.getColor(R.styleable.DateTimePickerComponent_hourColor, datePickerConfig.DefaultColor);
		datePickerConfig.MinuteColor = attributes.getColor(R.styleable.DateTimePickerComponent_minuteColor, datePickerConfig.DefaultColor);
		datePickerConfig.SecondColor = attributes.getColor(R.styleable.DateTimePickerComponent_secondColor, datePickerConfig.DefaultColor);
		datePickerConfig.PreparedItemsColor = attributes.getColor(R.styleable.DateTimePickerComponent_preparedItemsColor, datePickerConfig.DefaultColor);
		datePickerConfig.HourMinuteSeparatorColor = attributes.getColor(R.styleable.DateTimePickerComponent_hourMinuteSeparatorColor, datePickerConfig.DefaultColor);
		datePickerConfig.MinuteSecondSeparatorColor = attributes.getColor(R.styleable.DateTimePickerComponent_minuteSecondSeparatorColor, datePickerConfig.DefaultColor);

		/**
		 * TextSize Options
		 */
		datePickerConfig.DefaultTextSize = attributes.getDimensionPixelSize(R.styleable.DateTimePickerComponent_defaultTextSize, 40 /*(int) getTextSize()*/);
		datePickerConfig.YearTextSize = attributes.getDimensionPixelSize(R.styleable.DateTimePickerComponent_yearTextSize, datePickerConfig.DefaultTextSize);
		datePickerConfig.MonthTextSize = attributes.getDimensionPixelSize(R.styleable.DateTimePickerComponent_monthTextSize, datePickerConfig.DefaultTextSize);
		datePickerConfig.DayNumberTextSize = attributes.getDimensionPixelSize(R.styleable.DateTimePickerComponent_dayNumberTextSize, datePickerConfig.DefaultTextSize);
		datePickerConfig.DayNameTextSize = attributes.getDimensionPixelSize(R.styleable.DateTimePickerComponent_dayNameTextSize, datePickerConfig.DefaultTextSize);
		datePickerConfig.HourTextSize = attributes.getDimensionPixelSize(R.styleable.DateTimePickerComponent_hourTextSize, datePickerConfig.DefaultTextSize);
		datePickerConfig.HourMinuteSeparatorTextSize = attributes.getDimensionPixelSize(R.styleable.DateTimePickerComponent_hourMinuteSeparatorTextSize, datePickerConfig.DefaultTextSize);
		datePickerConfig.MinuteTextSize = attributes.getDimensionPixelSize(R.styleable.DateTimePickerComponent_minuteTextSize, datePickerConfig.DefaultTextSize);
		datePickerConfig.MinuteSecondSeparatorTextSize = attributes.getDimensionPixelSize(R.styleable.DateTimePickerComponent_minuteSecondSeparatorTextSize, datePickerConfig.DefaultTextSize);
		datePickerConfig.SecondTextSize = attributes.getDimensionPixelSize(R.styleable.DateTimePickerComponent_secondTextSize, datePickerConfig.DefaultTextSize);
		datePickerConfig.PreparedItemsTextSize = attributes.getDimensionPixelSize(R.styleable.DateTimePickerComponent_preparedItemsTextSize, datePickerConfig.DefaultTextSize);

		/**
		 * Selected Values
		 */
		datePickerConfig.SelectedYear = attributes.getInt(R.styleable.DateTimePickerComponent_selectedYear, now.getPersianYear());
		datePickerConfig.SelectedMonthFrom1 = attributes.getInteger(R.styleable.DateTimePickerComponent_selectedMonth, now.getPersianMonthIndexFrom1());
		datePickerConfig.SelectedDay = attributes.getInteger(R.styleable.DateTimePickerComponent_selectedDay, now.getPersianDay());
		datePickerConfig.SelectedHour = attributes.getInt(R.styleable.DateTimePickerComponent_selectedHour, now.getHour());
		datePickerConfig.SelectedMinute = attributes.getInteger(R.styleable.DateTimePickerComponent_selectedMinute, now.getMinute());
		datePickerConfig.SelectedSecond = attributes.getInteger(R.styleable.DateTimePickerComponent_selectedSecond, now.getSecond());

		/**
		 * Display Options
		 */
		datePickerConfig.SecondDisplay = attributes.getBoolean(R.styleable.DateTimePickerComponent_secondDisplay, true);
		datePickerConfig.DayNameDisplay = attributes.getBoolean(R.styleable.DateTimePickerComponent_dayNameDisplay, true);
		datePickerConfig.DateTimeMode = attributes.getInteger(R.styleable.DateTimePickerComponent_dateTimeMode, DateTimePickerComponent.DateTimeModes.All);
		datePickerConfig.MonthDisplayMode = DateTimePickerComponent.MonthDisplayModes.ByCode(attributes.getInteger(R.styleable.DateTimePickerComponent_monthDisplayMode, DateTimePickerComponent.MonthDisplayModes.ByName.Code));
		datePickerConfig.MonthDisplayTemplate = attributes.getString(R.styleable.DateTimePickerComponent_monthDisplayTemplate);
		datePickerConfig.NumberLanguage = NumberLanguages.ByCode(attributes.getInt(R.styleable.DateTimePickerComponent_numberLang, NumberLanguages.English.Code));
		datePickerConfig.HourMode = HourModes.ByCode(attributes.getInteger(R.styleable.DateTimePickerComponent_hourMode, HourModes.TwentyFour24Hours.Code));
		datePickerConfig.datePickerTimeMode = DatePickerTimeModes.ByCode(attributes.getInteger(R.styleable.DateTimePickerComponent_datePickerTimeMode, DatePickerTimeModes.Now.Code));

		/**
		 * LeadingZero Options
		 */
		datePickerConfig.LeadingZero = attributes.getBoolean(R.styleable.DateTimePickerComponent_leadingZero, true);
		datePickerConfig.DayNumberLeadingZero = attributes.getBoolean(R.styleable.DateTimePickerComponent_dayLeadingZero, datePickerConfig.LeadingZero);
		datePickerConfig.HourNumberLeadingZero = attributes.getBoolean(R.styleable.DateTimePickerComponent_hourLeadingZero, datePickerConfig.LeadingZero);
		datePickerConfig.MinuteNumberLeadingZero = attributes.getBoolean(R.styleable.DateTimePickerComponent_minuteLeadingZero, datePickerConfig.LeadingZero);
		datePickerConfig.SecondNumberLeadingZero = attributes.getBoolean(R.styleable.DateTimePickerComponent_secondLeadingZero, datePickerConfig.LeadingZero);

		/**
		 * Step Options
		 */
		datePickerConfig.HourStep = attributes.getInteger(R.styleable.DateTimePickerComponent_hourStep, datePickerConfig.HourStep);
		datePickerConfig.MinuteStep = attributes.getInteger(R.styleable.DateTimePickerComponent_minuteStep, datePickerConfig.MinuteStep);
		datePickerConfig.SecondStep = attributes.getInteger(R.styleable.DateTimePickerComponent_secondStep, datePickerConfig.SecondStep);

		/**
		 * backgrounds
		 */
		datePickerConfig.DateHeaderTextStyle.Background = attributes.getDrawable(R.styleable.DateTimePickerComponent_dateHeaderBackground);
		datePickerConfig.DateHeaderTextStyle.TextColor = attributes.getColor(R.styleable.DateTimePickerComponent_dateHeaderTextColor, datePickerConfig.DefaultColor);
		datePickerConfig.DateHeaderTextStyle.TextGravity = attributes.getInteger(R.styleable.DateTimePickerComponent_dateHeaderTextGravity, Gravity.NO_GRAVITY);
		datePickerConfig.DateHeaderTextStyle.TextSize = attributes.getDimensionPixelSize(R.styleable.DateTimePickerComponent_dateHeaderTextSize, datePickerConfig.DefaultTextSize);

		datePickerConfig.TimeHeaderTextStyle.Background = attributes.getDrawable(R.styleable.DateTimePickerComponent_timeHeaderBackground);
		datePickerConfig.TimeHeaderTextStyle.TextColor = attributes.getColor(R.styleable.DateTimePickerComponent_timeHeaderTextColor, datePickerConfig.DefaultColor);
		datePickerConfig.TimeHeaderTextStyle.TextGravity = attributes.getInteger(R.styleable.DateTimePickerComponent_timeHeaderTextGravity, Gravity.NO_GRAVITY);
		datePickerConfig.TimeHeaderTextStyle.TextSize = attributes.getDimensionPixelSize(R.styleable.DateTimePickerComponent_timeHeaderTextSize, datePickerConfig.DefaultTextSize);

		datePickerConfig.PreparedHeaderTextStyle.Background = attributes.getDrawable(R.styleable.DateTimePickerComponent_preparedHeaderBackground);
		datePickerConfig.PreparedHeaderTextStyle.TextColor = attributes.getColor(R.styleable.DateTimePickerComponent_preparedHeaderTextColor, datePickerConfig.DefaultColor);
		datePickerConfig.PreparedHeaderTextStyle.TextGravity = attributes.getInteger(R.styleable.DateTimePickerComponent_preparedHeaderTextGravity, Gravity.NO_GRAVITY);
		datePickerConfig.PreparedHeaderTextStyle.TextSize = attributes.getDimensionPixelSize(R.styleable.DateTimePickerComponent_preparedHeaderTextSize, datePickerConfig.DefaultTextSize);
		///////////////////

		/*datePickerConfig.SpecifiedMinYear = attributes.getInt(R.styleable.DateTimePickerComponent_minYear, 1300);
		datePickerConfig.SpecifiedMaxYear = attributes.getInt(R.styleable.DateTimePickerComponent_maxYear, 1400);

		datePickerConfig.DefaultColor = attributes.getColor(R.styleable.DateTimePickerComponent_defaultColor, ContextCompat.getColor(getContext(), black));
		datePickerConfig.YearColor = attributes.getColor(R.styleable.DateTimePickerComponent_yearColor, datePickerConfig.DefaultColor);
		datePickerConfig.MonthColor = attributes.getColor(R.styleable.DateTimePickerComponent_monthColor, datePickerConfig.DefaultColor);
		datePickerConfig.DayNumberColor = attributes.getColor(R.styleable.DateTimePickerComponent_dayNumberColor, datePickerConfig.DefaultColor);
		datePickerConfig.DayNameColor = attributes.getColor(R.styleable.DateTimePickerComponent_dayNameColor, datePickerConfig.DefaultColor);
		datePickerConfig.HourColor = attributes.getColor(R.styleable.DateTimePickerComponent_hourColor, datePickerConfig.DefaultColor);
		datePickerConfig.MinuteColor = attributes.getColor(R.styleable.DateTimePickerComponent_minuteColor, datePickerConfig.DefaultColor);
		datePickerConfig.SecondColor = attributes.getColor(R.styleable.DateTimePickerComponent_secondColor, datePickerConfig.DefaultColor);

		datePickerConfig.SelectedYear = attributes.getInt(R.styleable.DateTimePickerComponent_selectedYear, now.getPersianYear());
		datePickerConfig.SelectedMonthFrom1 = attributes.getInteger(R.styleable.DateTimePickerComponent_selectedMonth, now.getPersianMonthIndexFrom1());
		datePickerConfig.SelectedDay = attributes.getInteger(R.styleable.DateTimePickerComponent_selectedDay, now.getPersianDay());
		datePickerConfig.SelectedHour = attributes.getInt(R.styleable.DateTimePickerComponent_selectedHour, now.getHour());
		datePickerConfig.SelectedMinute = attributes.getInteger(R.styleable.DateTimePickerComponent_selectedMinute, now.getMinute());
		datePickerConfig.SelectedSecond = attributes.getInteger(R.styleable.DateTimePickerComponent_selectedSecond, now.getSecond());

		datePickerConfig.LeadingZero = attributes.getBoolean(R.styleable.DateTimePickerComponent_leadingZero, true);
		datePickerConfig.SecondDisplay = attributes.getBoolean(R.styleable.DateTimePickerComponent_secondDisplay, true);
		datePickerConfig.DayNameDisplay = attributes.getBoolean(R.styleable.DateTimePickerComponent_dayNameDisplay, true);
		datePickerConfig.DateTimeMode = attributes.getInteger(R.styleable.DateTimePickerComponent_dateTimeMode, DateTimeModes.All);
		datePickerConfig.MonthDisplayMode = DateTimePickerComponent.MonthDisplayModes.ByCode(attributes.getInteger(R.styleable.DateTimePickerComponent_monthDisplayMode, DateTimePickerComponent.MonthDisplayModes.ByName.Code));
		datePickerConfig.MonthDisplayTemplate = attributes.getString(R.styleable.DateTimePickerComponent_monthDisplayTemplate);

		datePickerConfig.DayNumberLeadingZero = attributes.getBoolean(R.styleable.DateTimePickerComponent_dayLeadingZero, datePickerConfig.DayNumberLeadingZero);
		datePickerConfig.HourNumberLeadingZero = attributes.getBoolean(R.styleable.DateTimePickerComponent_hourLeadingZero, datePickerConfig.HourNumberLeadingZero);
		datePickerConfig.MinuteNumberLeadingZero = attributes.getBoolean(R.styleable.DateTimePickerComponent_minuteLeadingZero, datePickerConfig.MinuteNumberLeadingZero);
		datePickerConfig.SecondNumberLeadingZero = attributes.getBoolean(R.styleable.DateTimePickerComponent_secondLeadingZero, datePickerConfig.SecondNumberLeadingZero);
		datePickerConfig.HourStep = attributes.getInteger(R.styleable.DateTimePickerComponent_hourStep, datePickerConfig.HourStep);
		datePickerConfig.MinuteStep = attributes.getInteger(R.styleable.DateTimePickerComponent_minuteStep, datePickerConfig.MinuteStep);
		datePickerConfig.SecondStep = attributes.getInteger(R.styleable.DateTimePickerComponent_secondStep, datePickerConfig.SecondStep);
		datePickerConfig.HourMode = HourModes.ByCode(attributes.getInteger(R.styleable.DateTimePickerComponent_hourMode, HourModes.TwentyFour24Hours.Code));*/

		InitValues(datePickerConfig);
		attributes.recycle();

		ModeRadioButton().setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
				DateLayout().setVisibility(DateRadioButton().isChecked() ? VISIBLE : GONE);
				TimeLayout().setVisibility(TimeRadioButton().isChecked() ? VISIBLE : GONE);
				PreparedLayout().setVisibility(PreparedRadioButton().isChecked() ? VISIBLE : GONE);
			}
		});

		super.InitializeLayout(attrs, defStyleAttr, defStyleRes);
	}

	void InitValues(DatePickerConfig config) {
		this.datePickerConfig = config;
		PersianCalendar now = new PersianCalendar();
		now.setTimeZone(TimeZone.getDefault());

		setTypeface(datePickerConfig.defaultTypeface);

		yearNumberPicker().setOnItemSelectedListener(yearNumberPicker_ItemSelectedListener());
		monthNumberPicker().setOnItemSelectedListener(monthNumberPicker_ItemSelectedListener());
		dayNumberPicker().setOnItemSelectedListener(dayNumberPicker_ItemSelectedListener());
		hourNumberPicker().setOnItemSelectedListener(hourNumberPicker_ItemSelectedListener());
		minuteNumberPicker().setOnItemSelectedListener(minuteNumberPicker_ItemSelectedListener());
		secondNumberPicker().setOnItemSelectedListener(secondNumberPicker_ItemSelectedListener());
		PreparedWheel().setOnItemSelectedListener(preparedWheel_ItemSelectedListener());

		yearNumberPicker().setItemTextColor(datePickerConfig.YearColor);
		monthNumberPicker().setItemTextColor(datePickerConfig.MonthColor);
		dayNumberPicker().setItemTextColor(datePickerConfig.DayNumberColor);
		dayNamePicker().setItemTextColor(datePickerConfig.DayNameColor);
		hourNumberPicker().setItemTextColor(datePickerConfig.HourColor);
		hourMinuteSeparator().setTextColor(datePickerConfig.HourMinuteSeparatorColor);
		minuteNumberPicker().setItemTextColor(datePickerConfig.MinuteColor);
		minuteSecondSeparator().setTextColor(datePickerConfig.MinuteSecondSeparatorColor);
		secondNumberPicker().setItemTextColor(datePickerConfig.SecondColor);
		PreparedWheel().setItemTextColor(datePickerConfig.PreparedItemsColor);

		yearNumberPicker().setItemTextSize(EnsureTextSize(datePickerConfig.YearTextSize, "YearTextSize"));
		monthNumberPicker().setItemTextSize(EnsureTextSize(datePickerConfig.MonthTextSize, "MonthTextSize"));
		dayNumberPicker().setItemTextSize(EnsureTextSize(datePickerConfig.DayNumberTextSize, "DayNumberTextSize"));
		dayNamePicker().setItemTextSize(EnsureTextSize(datePickerConfig.DayNameTextSize, "DayNameTextSize"));
		hourNumberPicker().setItemTextSize(EnsureTextSize(datePickerConfig.HourTextSize, "HourTextSize"));
		hourMinuteSeparator().setTextSize(TypedValue.COMPLEX_UNIT_PX, EnsureTextSize(datePickerConfig.HourMinuteSeparatorTextSize, "HourMinuteSeparatorTextSize"));
		minuteNumberPicker().setItemTextSize(EnsureTextSize(datePickerConfig.MinuteTextSize, "MinuteTextSize"));
		minuteSecondSeparator().setTextSize(TypedValue.COMPLEX_UNIT_PX, EnsureTextSize(datePickerConfig.MinuteSecondSeparatorTextSize, "MinuteSecondSeparatorTextSize"));
		secondNumberPicker().setItemTextSize(EnsureTextSize(datePickerConfig.SecondTextSize, "SecondTextSize"));
		PreparedWheel().setItemTextSize(EnsureTextSize(datePickerConfig.PreparedItemsTextSize, "PreparedItemsTextSize"));

		ViewGroup.LayoutParams layoutParams = ActiveRowSelector().getLayoutParams();
		layoutParams.height = datePickerConfig.DefaultTextSize * 2;
		ActiveRowSelector().setLayoutParams(layoutParams);

		TextStyle.Apply(DateRadioButton(), datePickerConfig.DateHeaderTextStyle);
		TextStyle.Apply(TimeRadioButton(), datePickerConfig.TimeHeaderTextStyle);
		TextStyle.Apply(PreparedRadioButton(), datePickerConfig.PreparedHeaderTextStyle);

		if (datePickerConfig.SpecifiedMinYear != null) {
			minYear = -1;
			if (datePickerConfig.SpecifiedMinYear <= 1000)
				minYear = now.getPersianYear() + datePickerConfig.SpecifiedMinYear;
			if (1300 <= datePickerConfig.SpecifiedMinYear)
				minYear = datePickerConfig.SpecifiedMinYear;
			if (1000 < datePickerConfig.SpecifiedMinYear && datePickerConfig.SpecifiedMinYear < 1300)
				throw new RuntimeException(String.format("MinYear (%s) Provided is not below 1000 to used as relative and not greater than 1300 to be used as fixed", datePickerConfig.SpecifiedMinYear));
			if (datePickerConfig.SpecifiedMinYear < -1000)
				throw new RuntimeException(String.format("MinYear (%s) Provided is less than -1000", datePickerConfig.SpecifiedMinYear));
		}

		if (datePickerConfig.SpecifiedMaxYear != null) {
			if (datePickerConfig.SpecifiedMaxYear < 1000)
				maxYear = now.getPersianYear() + datePickerConfig.SpecifiedMaxYear;
			if (1300 < datePickerConfig.SpecifiedMaxYear)
				maxYear = datePickerConfig.SpecifiedMaxYear;
			if (1000 < datePickerConfig.SpecifiedMaxYear && datePickerConfig.SpecifiedMaxYear < 1300)
				throw new RuntimeException(String.format("MaxYear (%s) Provided is not below 1000 to used as relative and not greater than 1300 to be used as fixed", datePickerConfig.SpecifiedMaxYear));
			if (datePickerConfig.SpecifiedMaxYear < -1000)
				throw new RuntimeException(String.format("MaxYear (%s) Provided is less than -1000", datePickerConfig.SpecifiedMaxYear));
		}

		if (maxYear < minYear)
			throw new RuntimeException(String.format("maxYear (%s) is less than minYear (%s)", maxYear, minYear));

		if (datePickerConfig.SelectedYear > maxYear || datePickerConfig.SelectedYear < minYear)
			throw new IllegalArgumentException(String.format(Locale.getDefault(), "Selected year (%d) must be between minYear(%d) and maxYear(%d)", datePickerConfig.SelectedYear, minYear, maxYear));

		if (datePickerConfig.SelectedMonthFrom1 < 1 || datePickerConfig.SelectedMonthFrom1 > 12)
			throw new IllegalArgumentException(String.format(Locale.getDefault(), "Selected month (%d) must be between 1 and 12", datePickerConfig.SelectedMonthFrom1));

		if (datePickerConfig.SelectedDay > 31 || datePickerConfig.SelectedDay < 1)
			throw new IllegalArgumentException(String.format(Locale.getDefault(), "Selected day (%d) must be between 1 and 31", datePickerConfig.SelectedDay));

		yearNumberPicker().setRangeValues(minYear, maxYear, false, datePickerConfig.NumberLanguage);
		switch (datePickerConfig.MonthDisplayMode) {
			case ByName:
				monthNumberPicker().setDisplayedValues(datePickerConfig.NumberLanguage, PersianMonths.Names());
				break;
			case ByNumber:
				monthNumberPicker().setRangeValues(1, 12, datePickerConfig.DayNumberLeadingZero, datePickerConfig.NumberLanguage);
				break;
			case ByTemplate:
				if (FarayanUtility.IsNullOrEmpty(datePickerConfig.MonthDisplayTemplate))
					throw new RuntimeException("MonthDisplayTemplate is null or empty while monthDisplayType set toi ByTemplate");
				monthNumberPicker().setDisplayedValues(datePickerConfig.NumberLanguage, PersianMonths.TemplateNames(datePickerConfig.MonthDisplayTemplate));
				break;
		}
		dayNumberPicker().setRangeValues(1, 31, datePickerConfig.DayNumberLeadingZero, datePickerConfig.NumberLanguage);

		dayNamePicker().setVisibility(datePickerConfig.DayNameDisplay ? VISIBLE : GONE);
		dayNamePicker().setDisplayedValues(datePickerConfig.NumberLanguage, WeekDays.PersianDayNames());
		dayNamePicker().setEnabled(false);

		switch (datePickerConfig.HourMode) {
			case Twelve12Hours:
				List<String> hours = new ArrayList<>();
				for (int h = 0; h < 24; h += datePickerConfig.HourStep) {
					String hour = datePickerConfig.HourNumberLeadingZero ? FarayanUtility.LeadingZero(h % 12, 2) : (h % 12 + "");
					String suffix = (h < 12 ? BeforeNoon : AfterNoon);
					hours.add(hour + " " + suffix);
				}
				hourNumberPicker().setDisplayedValues(datePickerConfig.NumberLanguage, hours);
				break;
			case TwentyFour24Hours:
				hourNumberPicker().setRangeValues(0, 23, datePickerConfig.HourNumberLeadingZero, datePickerConfig.NumberLanguage, datePickerConfig.HourStep);
				break;
		}
		minuteNumberPicker().setRangeValues(0, 59, datePickerConfig.MinuteNumberLeadingZero, datePickerConfig.NumberLanguage, datePickerConfig.MinuteStep);
		secondNumberPicker().setVisibility(datePickerConfig.SecondDisplay ? View.VISIBLE : View.GONE);
		minuteSecondSeparator().setVisibility(datePickerConfig.SecondDisplay ? View.VISIBLE : View.GONE);
		secondNumberPicker().setRangeValues(0, 59, datePickerConfig.SecondNumberLeadingZero, datePickerConfig.NumberLanguage, datePickerConfig.SecondStep);

		PersianDateTime persianDateTime = new PersianCalendar(
				datePickerConfig.SelectedYear,
				datePickerConfig.SelectedMonthFrom1,
				datePickerConfig.SelectedDay,
				datePickerConfig.SelectedHour,
				datePickerConfig.SelectedMinute,
				datePickerConfig.SelectedSecond,
				0,
				null
		);
		setSelectedPersianCalendar(persianDateTime);

		PreparedWheel().setAdapter(new PreparedDateTimeAdapter());
		PreparedWheel().setSelectedItemPosition(PreparedDates.Now.Position);

		if (DateTimeModes.ByCode(datePickerConfig.DateTimeMode) != null) {
			DateRadioButton().setVisibility(GONE);
			TimeRadioButton().setVisibility(GONE);
			PreparedRadioButton().setVisibility(GONE);
		} else {
			DateRadioButton().setVisibility(DateTimeModes.Date.Contained(datePickerConfig.DateTimeMode) ? VISIBLE : GONE);
			TimeRadioButton().setVisibility(DateTimeModes.Time.Contained(datePickerConfig.DateTimeMode) ? VISIBLE : GONE);
			PreparedRadioButton().setVisibility(DateTimeModes.Prepared.Contained(datePickerConfig.DateTimeMode) ? VISIBLE : GONE);
		}
	}

	private int EnsureTextSize(int textSize, String name) {
		if (textSize < 5)
			throw new RuntimeException(name + " is less than 5");
		return textSize;
	}

	private WheelPicker.OnItemSelectedListener preparedWheel_ItemSelectedListener() {
		return new WheelPicker.OnItemSelectedListener() {
			@Override
			public void onItemSelected(WheelPicker wheelPicker, Object o, int i) {
				PreparedDates preparedDate = (PreparedDates) PreparedWheel().getValue();
				PersianDateTime preparedDateValue = preparedDate.Provider.Value();
				yearNumberPicker().setValue(preparedDateValue.getPersianYear());
				monthNumberPicker().setSelectedItemPosition(preparedDateValue.getPersianMonthIndexFrom1() - 1);
				dayNumberPicker().setValue(preparedDateValue.getPersianDay());
				dayNamePicker().setValue(preparedDateValue.PersianMonthWeekIndex());
				hourNumberPicker().setSelectedItemPosition(calcIndexByStep(preparedDateValue.getHour(), datePickerConfig.HourStep));
				minuteNumberPicker().setSelectedItemPosition(calcIndexByStep(preparedDateValue.getMinute(), datePickerConfig.MinuteStep));
				secondNumberPicker().setSelectedItemPosition(calcIndexByStep(preparedDateValue.getSecond(), datePickerConfig.SecondStep));
				dateChanged();
			}

			@Override
			public void onCurrentItemOfScroll(WheelPicker wheelPicker, int i) {

			}
		};
	}


	public class PreparedDateTimeAdapter extends WheelPicker.Adapter {

		@Override
		public int getItemCount() {
			return PreparedDates.values().length;
		}

		@Override
		public Object getItem(int position) {
			PreparedDates preparedDate = PreparedDates.ByPosition(position);
			return preparedDate;
		}

		@Override
		public String getItemText(int position) {
			PreparedDates preparedDate = PreparedDates.ByPosition(position);
			return preparedDate.Displayable();
		}
	}

	final class DateChangedListener implements WheelPicker.OnItemSelectedListener {

		@Override
		public void onItemSelected(WheelPicker wheelPicker, Object o, int i) {
			dateChanged();
		}

		@Override
		public void onCurrentItemOfScroll(WheelPicker wheelPicker, int i) {

		}
	}

	private WheelPicker.OnItemSelectedListener dayNumberPicker_ItemSelectedListener() {
		return new DateChangedListener();
	}

	private WheelPicker.OnItemSelectedListener monthNumberPicker_ItemSelectedListener() {
		return new DateChangedListener();
	}

	private WheelPicker.OnItemSelectedListener yearNumberPicker_ItemSelectedListener() {
		return new DateChangedListener();
	}

	private WheelPicker.OnItemSelectedListener hourNumberPicker_ItemSelectedListener() {
		return new DateChangedListener();
	}

	private WheelPicker.OnItemSelectedListener minuteNumberPicker_ItemSelectedListener() {
		return new DateChangedListener();
	}

	private WheelPicker.OnItemSelectedListener secondNumberPicker_ItemSelectedListener() {
		return new DateChangedListener();
	}

	public PersianCalendar getSelectedPersianCalendar() {
		int year, month, day, hour, minute, second;

		year = (int) yearNumberPicker().getValue();
		month = monthNumberPicker().getCurrentItemPosition() + 1;
		day = dayNumberPicker().getCurrentItemPosition() + 1;

		if (DateTimeModes.Time.Contained(datePickerConfig.DateTimeMode)) {
			hour = hourNumberPicker().getCurrentItemPosition() * datePickerConfig.HourStep;
			minute = minuteNumberPicker().getCurrentItemPosition() * datePickerConfig.MinuteStep;
			second = secondNumberPicker().getCurrentItemPosition() * datePickerConfig.SecondStep;
		} else {
			switch (datePickerConfig.datePickerTimeMode) {
				case DayStart:
					hour = minute = second = 0;
					break;
				case Now:
					PersianCalendar now = new PersianCalendar();
					hour = now.getHour();
					minute = now.getMinute();
					second = now.getSecond();
					break;
				default:
					throw new RuntimeException(String.format("Unsupported DatePickerTimeMode: %s", datePickerConfig.datePickerTimeMode));
			}
		}

		return new PersianCalendar(year, month, day, hour, minute, second, 0, null);
	}

	public void setSelectedPersianCalendar(PersianDateTime value) {
		if (value == null)
			value = new PersianCalendar();

		yearNumberPicker().setValue(value.getPersianYear());
		monthNumberPicker().setSelectedItemPosition(value.getPersianMonthIndexFrom1() - 1);
		dayNumberPicker().setValue(value.getPersianDay());
		dayNamePicker().setSelectedItemPosition(value.PersianWeekDay().PersianDayIndex - 1);
		hourNumberPicker().setSelectedItemPosition(calcIndexByStep(value.getHour(), datePickerConfig.getHourStep()));
		minuteNumberPicker().setSelectedItemPosition(calcIndexByStep(value.getMinute(), datePickerConfig.getMinuteStep()));
		secondNumberPicker().setSelectedItemPosition(calcIndexByStep(value.getSecond(), datePickerConfig.getSecondStep()));
	}


	private int calcIndexByStep(int value, int step) {
		return value / step;
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);

		ss.datetime = this.getSelectedPersianCalendar().getTimeInMillis();
		return ss;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (!(state instanceof SavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}

		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());

		setSelectedPersianCalendar(new PersianCalendar(ss.datetime));
	}

	static class SavedState extends BaseSavedState {
		long datetime;

		SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			this.datetime = in.readLong();
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeLong(this.datetime);
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

	public void setTypeface(Typeface font) {
		yearNumberPicker().setTypeface(font);
		monthNumberPicker().setTypeface(font);
		dayNumberPicker().setTypeface(font);
		dayNamePicker().setTypeface(font);
		hourNumberPicker().setTypeface(font);
		hourMinuteSeparator().setTypeface(font);
		minuteNumberPicker().setTypeface(font);
		minuteSecondSeparator().setTypeface(font);
		secondNumberPicker().setTypeface(font);
		PreparedWheel().setTypeface(font);
	}
}
