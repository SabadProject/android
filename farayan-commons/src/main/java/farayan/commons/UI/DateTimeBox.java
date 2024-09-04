package farayan.commons.UI;

import static android.R.color.black;
import static android.R.color.darker_gray;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import farayan.commons.FarayanUtility;
import farayan.commons.PersianCalendar;
import farayan.commons.PersianDateTime;
import farayan.commons.R;
import farayan.commons.UI.Commons.DatePickerTimeModes;
import farayan.commons.UI.Commons.HourModes;
import farayan.commons.UI.Commons.NumberLanguages;
import farayan.commons.UI.Core.IGeneralEvent;
import farayan.commons.UI.Core.IGeneralEventProvider;

/**
 * به صورت یک متن ساده، تاریخ انتخابی را نشان می‌دهد
 * در صورتی که روی تاریخ یا آیکنِ انتخابِ کنارِ آن کلیک شود، دیالوگ انتخاب تاریخ نمایش داده می‌شود
 * دو تا ابزار دیگر هم داریم
 * اولی، دیالوگ انتخاب تاریخ است
 * دومی انتخابگر تاریخ است
 * دیالوگ انتخاب‌گر تاریخ در واقع همان انتخاب‌گر تاریخ را نشان می‌دهد
 */
public class DateTimeBox extends androidx.appcompat.widget.AppCompatTextView implements IGeneralEventProvider<Void, DateTimePickerComponent.Events, PersianDateTime>
{

	private IGeneralEvent<Void, DateTimePickerComponent.Events, PersianDateTime> DateFooter_EventHandler = new IGeneralEvent<Void, DateTimePickerComponent.Events, PersianDateTime>()
	{
		@Override
		public Void OnEvent(DateTimePickerComponent.Events command, PersianDateTime value) {
			setValue(value);
			if (TheEvent != null)
				TheEvent.OnEvent(command, value);
			return null;
		}
	};
	private IGeneralEvent<Void, DateTimePickerComponent.Events, PersianDateTime> TheEvent;


	public void DisplayPicker() {
		switch (dialogDisplayType) {
			case Standard:
				ShowDialog();
				break;
			case Footer:
				ShowFooter();
				break;
		}
	}

	@Override
	public void SetEventHandler(IGeneralEvent<Void, DateTimePickerComponent.Events, PersianDateTime> iEvent) {
		TheEvent = iEvent;
	}

	enum DialogDisplayTypes
	{
		Standard(0),
		Footer(1),
		;

		public final int Code;

		DialogDisplayTypes(int code) {
			Code = code;
		}

		public static DialogDisplayTypes ByCode(int code) {
			for (DialogDisplayTypes item : values()) {
				if (item.Code == code)
					return item;
			}
			return null;
		}
	}


	/**
	 * this component fields
	 */
	private int textColor;
	private int hintColor;
	private String hint = "انتخاب تاریخ";
	int pickerDrawableResourceID;
	private Drawable pickerDrawable;
	private ColorStateList pickerDrawableTintColor;
	private boolean pickerDrawableTintColorProvided = false;
	private PorterDuff.Mode pickerDrawableTintMode;
	private boolean pickerDrawableTintModeProvided;
	private DialogDisplayTypes dialogDisplayType;

	final DatePickerConfig datePickerConfig = new DatePickerConfig();
	final DateTimeDialogConfig dialogConfig = new DateTimeDialogConfig();

	public DateTimeBox(Context context) {
		super(context);
		InitializeLayout();
	}

	public DateTimeBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		LoadAttributes(context, attrs);
		InitializeLayout();
	}

	public DateTimeBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LoadAttributes(context, attrs);
		InitializeLayout();
	}

	protected void InitializeLayout() {
		Drawable[] drawables = getCompoundDrawables();
		applyPickerTintColor();
		if (drawables[0] == null) {
			drawables[0] = pickerDrawable;
			setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], drawables[3]);
		}
		this.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				DisplayPicker();
			}
		});
		setValue(getValue());
	}

	private void applyPickerTintColor() {
		if (pickerDrawable != null && (pickerDrawableTintColorProvided || pickerDrawableTintModeProvided)) {
			pickerDrawable = DrawableCompat.wrap(pickerDrawable).mutate();

			if (pickerDrawableTintColorProvided) {
				DrawableCompat.setTintList(pickerDrawable, pickerDrawableTintColor);
			}
			if (pickerDrawableTintModeProvided) {
				DrawableCompat.setTintMode(pickerDrawable, pickerDrawableTintMode);
			}
		}
	}


	@Override
	public boolean dispatchKeyEventPreIme(KeyEvent event) {
		FarayanUtility.Log(false, true, true, "dispatchKeyEventPreIme");
		return super.dispatchKeyEventPreIme(event);
	}


	public static PorterDuff.Mode intToMode(int val) {
		switch (val) {
			default:
			case 0:
				return PorterDuff.Mode.CLEAR;
			case 1:
				return PorterDuff.Mode.SRC;
			case 2:
				return PorterDuff.Mode.DST;
			case 3:
				return PorterDuff.Mode.SRC_OVER;
			case 4:
				return PorterDuff.Mode.DST_OVER;
			case 5:
				return PorterDuff.Mode.SRC_IN;
			case 6:
				return PorterDuff.Mode.DST_IN;
			case 7:
				return PorterDuff.Mode.SRC_OUT;
			case 8:
				return PorterDuff.Mode.DST_OUT;
			case 9:
				return PorterDuff.Mode.SRC_ATOP;
			case 10:
				return PorterDuff.Mode.DST_ATOP;
			case 11:
				return PorterDuff.Mode.XOR;
			case 16:
				return PorterDuff.Mode.DARKEN;
			case 17:
				return PorterDuff.Mode.LIGHTEN;
			case 13:
				return PorterDuff.Mode.MULTIPLY;
			case 14:
				return PorterDuff.Mode.SCREEN;
			case 12:
				return PorterDuff.Mode.ADD;
			case 15:
				return PorterDuff.Mode.OVERLAY;
		}
	}

	private void LoadAttributes(Context context, AttributeSet attrs) {
		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.DateTimeBox, 0, 0);
		try {
			hint = FarayanUtility.Or(attributes.getString(R.styleable.DateTimeBox_hint), hint);
			hintColor = attributes.getColor(R.styleable.DateTimeBox_hintColor, ContextCompat.getColor(getContext(), darker_gray));
			textColor = attributes.getColor(R.styleable.DateTimeBox_textColor, ContextCompat.getColor(getContext(), black));
			pickerDrawableResourceID = attributes.getResourceId(R.styleable.DateTimeBox_pickerDrawable, R.drawable.select);
			pickerDrawable = getResources().getDrawable(pickerDrawableResourceID, null);
			if (attributes.hasValue(R.styleable.DateTimeBox_pickerDrawableTintColor)) {
				pickerDrawableTintColor = attributes.getColorStateList(R.styleable.DateTimeBox_pickerDrawableTintColor);
				pickerDrawableTintColorProvided = true;
			}
			if (attributes.hasValue(R.styleable.DateTimeBox_pickerDrawableTintMode)) {
				pickerDrawableTintMode = intToMode(attributes.getInt(R.styleable.DateTimeBox_pickerDrawableTintMode, 0));
				pickerDrawableTintModeProvided = true;
			}

			dialogDisplayType = DialogDisplayTypes.ByCode(attributes.getInt(R.styleable.DateTimeBox_dialogType, DialogDisplayTypes.Standard.Code));

			dialogConfig.dialogHeaderText = FarayanUtility.Or(attributes.getString(R.styleable.DateTimeBox_dialogHeaderText), "تاریخ را انتخاب کنید");
			dialogConfig.dialogHeaderStyle.TextColor = attributes.getColor(R.styleable.DateTimeBox_dialogHeaderTextColor, Color.BLACK);
			dialogConfig.dialogHeaderStyle.TextSize = attributes.getDimensionPixelSize(R.styleable.DateTimeBox_dialogHeaderTextSize, (int) getTextSize());
			dialogConfig.dialogHeaderStyle.TextGravity = attributes.getInteger(R.styleable.DateTimeBox_dialogHeaderTextGravity, Gravity.NO_GRAVITY);
			dialogConfig.dialogHeaderStyle.Background = attributes.getDrawable(R.styleable.DateTimeBox_dialogHeaderBackground);

			dialogConfig.dialogButtonText = FarayanUtility.Or(attributes.getString(R.styleable.DateTimeBox_dialogButtonText), "انتخاب تاریخ");
			dialogConfig.dialogButtonStyle.TextColor = attributes.getColor(R.styleable.DateTimeBox_dialogButtonTextColor, Color.BLACK);
			dialogConfig.dialogButtonStyle.TextGravity = attributes.getInteger(R.styleable.DateTimeBox_dialogButtonTextGravity, Gravity.NO_GRAVITY);
			dialogConfig.dialogButtonStyle.TextSize = attributes.getDimensionPixelSize(R.styleable.DateTimeBox_dialogButtonTextSize, (int) getTextSize());
			dialogConfig.dialogButtonStyle.Background = attributes.getDrawable(R.styleable.DateTimeBox_dialogButtonBackground);

			dialogConfig.Background = attributes.getDrawable(R.styleable.DateTimeBox_dialogBackground);

			PersianCalendar now = new PersianCalendar();

			/**
			 * Min and Max
			 */
			datePickerConfig.SpecifiedMinYear = attributes.getInt(R.styleable.DateTimeBox_minYear, 1300);
			datePickerConfig.SpecifiedMaxYear = attributes.getInt(R.styleable.DateTimeBox_maxYear, 1500);

			/**
			 * Colors
			 */
			datePickerConfig.DefaultColor = attributes.getColor(R.styleable.DateTimeBox_defaultColor, ContextCompat.getColor(getContext(), black));
			datePickerConfig.YearColor = attributes.getColor(R.styleable.DateTimeBox_yearColor, datePickerConfig.DefaultColor);
			datePickerConfig.MonthColor = attributes.getColor(R.styleable.DateTimeBox_monthColor, datePickerConfig.DefaultColor);
			datePickerConfig.DayNumberColor = attributes.getColor(R.styleable.DateTimeBox_dayNumberColor, datePickerConfig.DefaultColor);
			datePickerConfig.DayNameColor = attributes.getColor(R.styleable.DateTimeBox_dayNameColor, datePickerConfig.DefaultColor);
			datePickerConfig.HourColor = attributes.getColor(R.styleable.DateTimeBox_hourColor, datePickerConfig.DefaultColor);
			datePickerConfig.MinuteColor = attributes.getColor(R.styleable.DateTimeBox_minuteColor, datePickerConfig.DefaultColor);
			datePickerConfig.SecondColor = attributes.getColor(R.styleable.DateTimeBox_secondColor, datePickerConfig.DefaultColor);
			datePickerConfig.PreparedItemsColor = attributes.getColor(R.styleable.DateTimeBox_preparedItemsColor, datePickerConfig.DefaultColor);
			datePickerConfig.HourMinuteSeparatorColor = attributes.getColor(R.styleable.DateTimeBox_hourMinuteSeparatorColor, datePickerConfig.DefaultColor);
			datePickerConfig.MinuteSecondSeparatorColor = attributes.getColor(R.styleable.DateTimeBox_minuteSecondSeparatorColor, datePickerConfig.DefaultColor);

			/**
			 * TextSize Options
			 */
			datePickerConfig.DefaultTextSize = attributes.getDimensionPixelSize(R.styleable.DateTimeBox_defaultTextSize, 40 /*(int) getTextSize()*/);
			datePickerConfig.YearTextSize = attributes.getDimensionPixelSize(R.styleable.DateTimeBox_yearTextSize, datePickerConfig.DefaultTextSize);
			datePickerConfig.MonthTextSize = attributes.getDimensionPixelSize(R.styleable.DateTimeBox_monthTextSize, datePickerConfig.DefaultTextSize);
			datePickerConfig.DayNumberTextSize = attributes.getDimensionPixelSize(R.styleable.DateTimeBox_dayNumberTextSize, datePickerConfig.DefaultTextSize);
			datePickerConfig.DayNameTextSize = attributes.getDimensionPixelSize(R.styleable.DateTimeBox_dayNameTextSize, datePickerConfig.DefaultTextSize);
			datePickerConfig.HourTextSize = attributes.getDimensionPixelSize(R.styleable.DateTimeBox_hourTextSize, datePickerConfig.DefaultTextSize);
			datePickerConfig.HourMinuteSeparatorTextSize = attributes.getDimensionPixelSize(R.styleable.DateTimeBox_hourMinuteSeparatorTextSize, datePickerConfig.DefaultTextSize);
			datePickerConfig.MinuteTextSize = attributes.getDimensionPixelSize(R.styleable.DateTimeBox_minuteTextSize, datePickerConfig.DefaultTextSize);
			datePickerConfig.MinuteSecondSeparatorTextSize = attributes.getDimensionPixelSize(R.styleable.DateTimeBox_minuteSecondSeparatorTextSize, datePickerConfig.DefaultTextSize);
			datePickerConfig.SecondTextSize = attributes.getDimensionPixelSize(R.styleable.DateTimeBox_secondTextSize, datePickerConfig.DefaultTextSize);
			datePickerConfig.PreparedItemsTextSize = attributes.getDimensionPixelSize(R.styleable.DateTimeBox_preparedItemsTextSize, datePickerConfig.DefaultTextSize);

			/**
			 * Selected Values
			 */
			datePickerConfig.SelectedYear = attributes.getInt(R.styleable.DateTimeBox_selectedYear, now.getPersianYear());
			datePickerConfig.SelectedMonthFrom1 = attributes.getInteger(R.styleable.DateTimeBox_selectedMonth, now.getPersianMonthIndexFrom1());
			datePickerConfig.SelectedDay = attributes.getInteger(R.styleable.DateTimeBox_selectedDay, now.getPersianDay());
			datePickerConfig.SelectedHour = attributes.getInt(R.styleable.DateTimeBox_selectedHour, now.getHour());
			datePickerConfig.SelectedMinute = attributes.getInteger(R.styleable.DateTimeBox_selectedMinute, now.getMinute());
			datePickerConfig.SelectedSecond = attributes.getInteger(R.styleable.DateTimeBox_selectedSecond, now.getSecond());

			/**
			 * Display Options
			 */
			datePickerConfig.SecondDisplay = attributes.getBoolean(R.styleable.DateTimeBox_secondDisplay, true);
			datePickerConfig.DayNameDisplay = attributes.getBoolean(R.styleable.DateTimeBox_dayNameDisplay, true);
			datePickerConfig.DateTimeMode = attributes.getInteger(R.styleable.DateTimeBox_dateTimeMode, DateTimePickerComponent.DateTimeModes.All);
			datePickerConfig.MonthDisplayMode = DateTimePickerComponent.MonthDisplayModes.ByCode(attributes.getInteger(R.styleable.DateTimeBox_monthDisplayMode, DateTimePickerComponent.MonthDisplayModes.ByName.Code));
			datePickerConfig.MonthDisplayTemplate = attributes.getString(R.styleable.DateTimeBox_monthDisplayTemplate);
			datePickerConfig.NumberLanguage = NumberLanguages.ByCode(attributes.getInt(R.styleable.DateTimeBox_numberLang, NumberLanguages.English.Code));
			datePickerConfig.HourMode = HourModes.ByCode(attributes.getInteger(R.styleable.DateTimeBox_hourMode, HourModes.TwentyFour24Hours.Code));
			datePickerConfig.datePickerTimeMode = DatePickerTimeModes.ByCode(attributes.getInteger(R.styleable.DateTimeBox_datePickerTimeMode, DatePickerTimeModes.Now.Code));

			/**
			 * LeadingZero Options
			 */
			datePickerConfig.LeadingZero = attributes.getBoolean(R.styleable.DateTimeBox_leadingZero, true);
			datePickerConfig.DayNumberLeadingZero = attributes.getBoolean(R.styleable.DateTimeBox_dayLeadingZero, datePickerConfig.LeadingZero);
			datePickerConfig.HourNumberLeadingZero = attributes.getBoolean(R.styleable.DateTimeBox_hourLeadingZero, datePickerConfig.LeadingZero);
			datePickerConfig.MinuteNumberLeadingZero = attributes.getBoolean(R.styleable.DateTimeBox_minuteLeadingZero, datePickerConfig.LeadingZero);
			datePickerConfig.SecondNumberLeadingZero = attributes.getBoolean(R.styleable.DateTimeBox_secondLeadingZero, datePickerConfig.LeadingZero);

			/**
			 * Step Options
			 */
			datePickerConfig.HourStep = attributes.getInteger(R.styleable.DateTimeBox_hourStep, datePickerConfig.HourStep);
			datePickerConfig.MinuteStep = attributes.getInteger(R.styleable.DateTimeBox_minuteStep, datePickerConfig.MinuteStep);
			datePickerConfig.SecondStep = attributes.getInteger(R.styleable.DateTimeBox_secondStep, datePickerConfig.SecondStep);

			/**
			 * backgrounds
			 */
			datePickerConfig.DateHeaderTextStyle.Background = attributes.getDrawable(R.styleable.DateTimeBox_dateHeaderBackground);
			datePickerConfig.DateHeaderTextStyle.TextColor = attributes.getColor(R.styleable.DateTimeBox_dateHeaderTextColor, datePickerConfig.DefaultColor);
			datePickerConfig.DateHeaderTextStyle.TextGravity = attributes.getInteger(R.styleable.DateTimeBox_dateHeaderTextGravity, Gravity.NO_GRAVITY);
			datePickerConfig.DateHeaderTextStyle.TextSize = attributes.getDimensionPixelSize(R.styleable.DateTimeBox_dateHeaderTextSize, datePickerConfig.DefaultTextSize);

			datePickerConfig.TimeHeaderTextStyle.Background = attributes.getDrawable(R.styleable.DateTimeBox_timeHeaderBackground);
			datePickerConfig.TimeHeaderTextStyle.TextColor = attributes.getColor(R.styleable.DateTimeBox_timeHeaderTextColor, datePickerConfig.DefaultColor);
			datePickerConfig.TimeHeaderTextStyle.TextGravity = attributes.getInteger(R.styleable.DateTimeBox_timeHeaderTextGravity, Gravity.NO_GRAVITY);
			datePickerConfig.TimeHeaderTextStyle.TextSize = attributes.getDimensionPixelSize(R.styleable.DateTimeBox_timeHeaderTextSize, datePickerConfig.DefaultTextSize);

			datePickerConfig.PreparedHeaderTextStyle.Background = attributes.getDrawable(R.styleable.DateTimeBox_preparedHeaderBackground);
			datePickerConfig.PreparedHeaderTextStyle.TextColor = attributes.getColor(R.styleable.DateTimeBox_preparedHeaderTextColor, datePickerConfig.DefaultColor);
			datePickerConfig.PreparedHeaderTextStyle.TextGravity = attributes.getInteger(R.styleable.DateTimeBox_preparedHeaderTextGravity, Gravity.NO_GRAVITY);
			datePickerConfig.PreparedHeaderTextStyle.TextSize = attributes.getDimensionPixelSize(R.styleable.DateTimeBox_preparedHeaderTextSize, datePickerConfig.DefaultTextSize);
		} finally {
			attributes.recycle();
		}
	}

	private void ShowDialog() {
		datePickerConfig.defaultTypeface = getTypeface();
		PersianDateTime pdt = getValue() == null ? new PersianDateTime() : getValue();
		datePickerConfig.SelectedYear = pdt.getPersianYear();
		datePickerConfig.SelectedMonthFrom1 = pdt.getPersianMonthIndexFrom1();
		datePickerConfig.SelectedDay = pdt.getPersianDay();
		datePickerConfig.SelectedHour = pdt.getHour();
		datePickerConfig.SelectedMinute = pdt.getMinute();
		datePickerConfig.SelectedSecond = pdt.getSecond();

		DateTimeDialogComponent dialog = new DateTimeDialogComponent(
				getContext(),
				datePickerConfig,
				dialogConfig,
				DateFooter_EventHandler
		);
		dialog.ShowDialog();
	}

	private void ShowFooter() {
		datePickerConfig.defaultTypeface = getTypeface();
		DateTimeBottomSheetComponent dateFooterComponent = new DateTimeBottomSheetComponent(
				getContext(),
				datePickerConfig,
				dialogConfig,
				DateFooter_EventHandler
		);
		dateFooterComponent.ShowDialog();
	}

	public PersianDateTime getValue() {
		return Value;
	}

	public void setValue(PersianDateTime value) {
		Value = value;
		setText(value == null ? hint : value.getPersianDateTimeSentence(true));
		setTextColor(value == null ? hintColor : textColor);
	}

	@Override
	public void setTypeface(Typeface tf) {
		super.setTypeface(tf);
	}

	private PersianDateTime Value;
}
