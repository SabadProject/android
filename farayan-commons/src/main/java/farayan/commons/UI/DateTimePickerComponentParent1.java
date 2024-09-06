package farayan.commons.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import farayan.commons.FarayanUtility;
import farayan.commons.R;
import farayan.commons.UI.Core.FarayanCommonBaseComponent;

public abstract class DateTimePickerComponentParent1 extends FarayanCommonBaseComponent
{
	private View rootView = null;

	protected static final String tag = "DateTimePickerComponentParent";

	private void LoadLayout(Context context) {
		Ctx = context;
		NullLoadedViews();
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		long start = System.currentTimeMillis();
		rootView = inflater.inflate(R.layout.date_time_picker, this);
		long finish = System.currentTimeMillis();
		Log.i("Performance", String.format("inflating date_time_picker takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));
		FarayanUtility.OverrideFonts(rootView, getDefaultFont());
	}


	Context Ctx;

	public DateTimePickerComponentParent1(Context context) {
		this(context, null);
	}

	public DateTimePickerComponentParent1(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.dateTimePickerStyle);
	}

	public DateTimePickerComponentParent1(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs, defStyle, R.style.FarayanCommonsDateTimePickerStyle);
	}

	public DateTimePickerComponentParent1(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		LoadLayout(context);
		InitializeLayout(attrs, defStyleAttr, defStyleRes);
	}

	private void NullLoadedViews() {
		m_ModeRadioButton = null;
		m_DateRadioButton = null;
		m_TimeRadioButton = null;
		m_PreparedRadioButton = null;
		m_DateLayout = null;
		m_yearNumberPicker = null;
		m_monthNumberPicker = null;
		m_dayNumberPicker = null;
		m_dayNamePicker = null;
		m_TimeLayout = null;
		m_hourNumberPicker = null;
		m_hourMinuteSeparator = null;
		m_minuteNumberPicker = null;
		m_minuteSecondSeparator = null;
		m_secondNumberPicker = null;
		m_PreparedLayout = null;
		m_PreparedWheel = null;
		m_ActiveRowSelector = null;
	}

	private RadioGroup m_ModeRadioButton;

	protected RadioGroup ModeRadioButton() {
		if (m_ModeRadioButton == null)
			m_ModeRadioButton = (RadioGroup) rootView.findViewById(R.id.ModeRadioButton);
		return m_ModeRadioButton;
	}

	private RadioButton m_DateRadioButton;

	protected RadioButton DateRadioButton() {
		if (m_DateRadioButton == null)
			m_DateRadioButton = (RadioButton) rootView.findViewById(R.id.DateRadioButton);
		return m_DateRadioButton;
	}

	private RadioButton m_TimeRadioButton;

	protected RadioButton TimeRadioButton() {
		if (m_TimeRadioButton == null)
			m_TimeRadioButton = (RadioButton) rootView.findViewById(R.id.TimeRadioButton);
		return m_TimeRadioButton;
	}

	private RadioButton m_PreparedRadioButton;

	protected RadioButton PreparedRadioButton() {
		if (m_PreparedRadioButton == null)
			m_PreparedRadioButton = (RadioButton) rootView.findViewById(R.id.PreparedRadioButton);
		return m_PreparedRadioButton;
	}

	private LinearLayout m_DateLayout;

	protected LinearLayout DateLayout() {
		if (m_DateLayout == null)
			m_DateLayout = (LinearLayout) rootView.findViewById(R.id.DateLayout);
		return m_DateLayout;
	}

	private farayan.commons.UI.Wheel m_yearNumberPicker;

	protected farayan.commons.UI.Wheel yearNumberPicker() {
		if (m_yearNumberPicker == null)
			m_yearNumberPicker = (farayan.commons.UI.Wheel) rootView.findViewById(R.id.yearNumberPicker);
		return m_yearNumberPicker;
	}

	private farayan.commons.UI.Wheel m_monthNumberPicker;

	protected farayan.commons.UI.Wheel monthNumberPicker() {
		if (m_monthNumberPicker == null)
			m_monthNumberPicker = (farayan.commons.UI.Wheel) rootView.findViewById(R.id.monthNumberPicker);
		return m_monthNumberPicker;
	}

	private farayan.commons.UI.Wheel m_dayNumberPicker;

	protected farayan.commons.UI.Wheel dayNumberPicker() {
		if (m_dayNumberPicker == null)
			m_dayNumberPicker = (farayan.commons.UI.Wheel) rootView.findViewById(R.id.dayNumberPicker);
		return m_dayNumberPicker;
	}

	private farayan.commons.UI.Wheel m_dayNamePicker;

	protected farayan.commons.UI.Wheel dayNamePicker() {
		if (m_dayNamePicker == null)
			m_dayNamePicker = (farayan.commons.UI.Wheel) rootView.findViewById(R.id.dayNamePicker);
		return m_dayNamePicker;
	}

	private LinearLayout m_TimeLayout;

	protected LinearLayout TimeLayout() {
		if (m_TimeLayout == null)
			m_TimeLayout = (LinearLayout) rootView.findViewById(R.id.TimeLayout);
		return m_TimeLayout;
	}

	private farayan.commons.UI.Wheel m_hourNumberPicker;

	protected farayan.commons.UI.Wheel hourNumberPicker() {
		if (m_hourNumberPicker == null)
			m_hourNumberPicker = (farayan.commons.UI.Wheel) rootView.findViewById(R.id.hourNumberPicker);
		return m_hourNumberPicker;
	}

	private TextView m_hourMinuteSeparator;

	protected TextView hourMinuteSeparator() {
		if (m_hourMinuteSeparator == null)
			m_hourMinuteSeparator = (TextView) rootView.findViewById(R.id.hourMinuteSeparator);
		return m_hourMinuteSeparator;
	}

	private farayan.commons.UI.Wheel m_minuteNumberPicker;

	protected farayan.commons.UI.Wheel minuteNumberPicker() {
		if (m_minuteNumberPicker == null)
			m_minuteNumberPicker = (farayan.commons.UI.Wheel) rootView.findViewById(R.id.minuteNumberPicker);
		return m_minuteNumberPicker;
	}

	private TextView m_minuteSecondSeparator;

	protected TextView minuteSecondSeparator() {
		if (m_minuteSecondSeparator == null)
			m_minuteSecondSeparator = (TextView) rootView.findViewById(R.id.minuteSecondSeparator);
		return m_minuteSecondSeparator;
	}

	private farayan.commons.UI.Wheel m_secondNumberPicker;

	protected farayan.commons.UI.Wheel secondNumberPicker() {
		if (m_secondNumberPicker == null)
			m_secondNumberPicker = (farayan.commons.UI.Wheel) rootView.findViewById(R.id.secondNumberPicker);
		return m_secondNumberPicker;
	}

	private LinearLayout m_PreparedLayout;

	protected LinearLayout PreparedLayout() {
		if (m_PreparedLayout == null)
			m_PreparedLayout = (LinearLayout) rootView.findViewById(R.id.PreparedLayout);
		return m_PreparedLayout;
	}

	private farayan.commons.UI.Wheel m_PreparedWheel;

	protected farayan.commons.UI.Wheel PreparedWheel() {
		if (m_PreparedWheel == null)
			m_PreparedWheel = (farayan.commons.UI.Wheel) rootView.findViewById(R.id.PreparedWheel);
		return m_PreparedWheel;
	}

	private View m_ActiveRowSelector;

	protected View ActiveRowSelector() {
		if (m_ActiveRowSelector == null)
			m_ActiveRowSelector = (View) rootView.findViewById(R.id.ActiveRowSelector);
		return m_ActiveRowSelector;
	}

	protected void InitializeComponents() {
	}

	protected void InitializeLayout(@Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		InitializeComponents();
	}
}
