package farayan.commons.UI;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;

import farayan.commons.FarayanBaseApp;
import farayan.commons.FarayanUtility;
import farayan.commons.R;
import farayan.commons.UI.Core.ICommandEvent;
import farayan.commons.UI.Core.ICommandEventProvider;

/**
 * جعبه ورودی عدد و پول
 */
public class CurrencyBoxComponent extends CurrencyBoxComponentParent implements ICommandEventProvider<Double>
{

	private static final String tag = "CurrencyBoxComponent";
	private final boolean SupportNegative;
	private final int FloatingPointPrecision;

	public double GetValue() {
		return lastValue;
	}

	@Override
	public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
		((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
		return NaturalNumberEditText().requestFocus();
	}

	private int DigitsCount;
	private double lastValue = 0;
	private boolean AllowPickers;
	private ColorStateList TextColor;

	public CurrencyBoxComponent(Context context) {
		this(context, null);
	}

	public CurrencyBoxComponent(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CurrencyBoxComponent, 0, 0);
		try {
			String prefix = attributes.getString(R.styleable.CurrencyBoxComponent_prefix);
			PrefixTextView().setText(prefix);

			String suffix = attributes.getString(R.styleable.CurrencyBoxComponent_suffix);
			SuffixTextView().setText(suffix);

			boolean supportFloatingPoint = attributes.getBoolean(R.styleable.CurrencyBoxComponent_supportFloatingPoint, false);
			setFloatingPoint(supportFloatingPoint);

			FloatingPointPrecision = attributes.getInt(R.styleable.CurrencyBoxComponent_FloatingPointPrecision, 3);
			if (FloatingPointPrecision < 0 || FloatingPointPrecision > 9)
				throw new RuntimeException(String.format("FloatingPointPrecision %s is not supported. must be between 0 ~ 9", FloatingPointPrecision));

			int digitsCount = attributes.getInt(R.styleable.CurrencyBoxComponent_digitsCount, supportFloatingPoint ? 9 : 12);
			setDigitsCount(digitsCount);

			boolean readOnly = attributes.getBoolean(R.styleable.CurrencyBoxComponent_readOnly, false);
			setReadOnly(readOnly);

			SupportNegative = attributes.getBoolean(R.styleable.CurrencyBoxComponent_supportsNegative, false);
			setSupportNegative(SupportNegative);

			AllowPickers = attributes.getBoolean(R.styleable.CurrencyBoxComponent_allowPickers, false);
			DisplayPickerCheckBox().setVisibility(AllowPickers ? View.VISIBLE : View.GONE);

			TextColor = attributes.getColorStateList(R.styleable.CurrencyBoxComponent_textColor);
			if (TextColor != null) {
				DecimalPartEditText().setTextColor(TextColor);
				NaturalNumberEditText().setTextColor(TextColor);
				PrefixTextView().setTextColor(TextColor);
				SuffixTextView().setTextColor(TextColor);
			}

			int fontSize = attributes.getInt(R.styleable.CurrencyBoxComponent_fontSize, 18);
			setFontSize(fontSize);

		} finally {
			attributes.recycle();
		}
	}

	private void setSupportNegative(boolean supportNegative) {
		SignTextView().setVisibility(supportNegative ? VISIBLE : GONE);
	}

	private void setFontSize(int fontSize) {
		PrefixTextView().setTextSize(fontSize);
		DecimalPartEditText().setTextSize(fontSize);
		DecimalSeparatorTextView().setTextSize(fontSize);
		NaturalNumberEditText().setTextSize(fontSize);
		SuffixTextView().setTextSize(fontSize);
	}

	boolean SupportDecimal = false;

	public void setFloatingPoint(boolean supportFloatingPoint) {
		SupportDecimal = supportFloatingPoint;
		setDecimalSupportInternal();
	}

	private void setDecimalSupportInternal() {
		FloatingPointSeparatorTextView().setVisibility(SupportDecimal ? View.VISIBLE : View.GONE);
		FloatingPointNumberPicker().setVisibility(SupportDecimal ? View.VISIBLE : View.GONE);
		DecimalPartEditText().setVisibility(SupportDecimal ? View.VISIBLE : View.GONE);
		DecimalSeparatorTextView().setVisibility(SupportDecimal ? View.VISIBLE : View.GONE);
	}

	private boolean ReadOnly;

	private void setReadOnly(boolean readOnly) {
		ReadOnly = readOnly;
		NaturalNumberEditText().setEnabled(!ReadOnly);
		Level1NumberPicker().setEnabled(!ReadOnly);
		Level2NumberPicker().setEnabled(!ReadOnly);
		Level3NumberPicker().setEnabled(!ReadOnly);
		Level4NumberPicker().setEnabled(!ReadOnly);
		FloatingPointNumberPicker().setEnabled(!ReadOnly);
	}

	@Override
	protected void InitializeComponents() {
		//NaturalNumberEditText().setOnFocusChangeListener(FarayanUtility.HideKeyboardOnFocusLose);
		//DecimalPartEditText().setOnFocusChangeListener(FarayanUtility.HideKeyboardOnFocusLose);
		NumberPickerChanged();

		SignTextView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SignTextView().getText().toString().equalsIgnoreCase("+")) {
					SignTextView().setText("-");
				} else {
					SignTextView().setText("+");
				}
				SetValueInternal(-GetValueFromEditText());
			}
		});

		DisplayPickerCheckBox().setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) {
				if (AllowPickers)
					PickerLayout().setVisibility(checked ? View.VISIBLE : View.GONE);
			}
		});
		PrefixTextView().setTypeface(FarayanBaseApp.getFont());
		NaturalNumberEditText().setTypeface(FarayanBaseApp.getFont());
		DecimalPartEditText().setTypeface(FarayanBaseApp.getFont());
		DecimalSeparatorTextView().setTypeface(FarayanBaseApp.getFont());
		SuffixTextView().setTypeface(FarayanBaseApp.getFont());

		NumberPicker.Formatter formatter = new NumberPicker.Formatter() {

			@Override
			public String format(int value) {
				return FarayanUtility.ConvertNumbersToPersian(FarayanUtility.LeadingZero(value, 3));
			}
		};

		Level1NumberPicker().setMinValue(0);
		Level1NumberPicker().setMaxValue(999);
		Level1NumberPicker().setFormatter(formatter);

		Level2NumberPicker().setMinValue(0);
		Level2NumberPicker().setMaxValue(999);
		Level2NumberPicker().setFormatter(formatter);

		Level3NumberPicker().setMinValue(0);
		Level3NumberPicker().setMaxValue(999);
		Level3NumberPicker().setFormatter(formatter);

		Level4NumberPicker().setMinValue(0);
		Level4NumberPicker().setMaxValue(999);
		Level4NumberPicker().setFormatter(formatter);

		FloatingPointNumberPicker().setMinValue(0);
		FloatingPointNumberPicker().setMaxValue(999);
		FloatingPointNumberPicker().setFormatter(formatter);

		Level1NumberPicker().setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				NumberPickerChanged();
			}
		});
		Level2NumberPicker().setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				NumberPickerChanged();
			}
		});
		Level3NumberPicker().setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				NumberPickerChanged();
			}
		});
		Level4NumberPicker().setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				NumberPickerChanged();
			}
		});
		FloatingPointNumberPicker().setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				NumberPickerChanged();
			}
		});

		DecimalPartEditText().addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				DecimalNumberEditTextOnTextChanged(s, start, before, count);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		NaturalNumberEditText().addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				NaturalNumberEditTextOnTextChanged(s, start, before, count);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	protected void DecimalNumberEditTextOnTextChanged(CharSequence s, int start, int before, int count) {
		if (SkipChangedEvent)
			return;
		double value = GetValueFromEditText();
		Log.i(tag, String.format("Text (%s)", s));
		SetValueInternal(value);
	}

	protected void NaturalNumberEditTextOnTextChanged(CharSequence s, int start, int before, int count) {
		if (SkipChangedEvent)
			return;
		double value = GetValueFromEditText();
		Log.i(tag, String.format("Text (%s)", s));
		SetValueInternal(value);
	}

	protected void NumberPickerChanged() {
		if (SkipNumberPickerChange)
			return;
		Log.i(tag, String.format("Calculate"));

		double value = GetValueFromNumberPickers();
		SetValueInternal(value);
	}

	private double GetValueFromNumberPickers() {
		String text = "";
		text += FarayanUtility.LeadingZero(Level4NumberPicker().getValue(), 3);
		text += FarayanUtility.LeadingZero(Level3NumberPicker().getValue(), 3);
		text += FarayanUtility.LeadingZero(Level2NumberPicker().getValue(), 3);
		text += FarayanUtility.LeadingZero(Level1NumberPicker().getValue(), 3);
		if (SupportDecimal && FloatingPointNumberPicker().getValue() > 0)
			text += "." + FloatingPointNumberPicker().getValue();

		double value = FarayanUtility.TryParseDouble(text, 0);
		return value;
	}

	public void SetValue(double value) {
		SetValueInternal(value);
	}

	private void SetValueInternal(double value) {
		/*Log.i(tag, String.format("SetValueInternal. value (%s)", value));
		if (!SupportNegative)
			value = Math.abs(value);

		if (value != GetValueFromNumberPickers()) {
			SetValueForNumberPicker(value);
		}
		SetValueForEditText(value);*/
		double finalValue = RoundByPrecision(value);
		double currentValue = GetValueFromEditText();

		//if (finalValue != currentValue) {
		SetValueForEditText(value);
		//}
		if (value != lastValue) {
			lastValue = value;
			if (ValueChangedEvent != null)
				ValueChangedEvent.OnEvent("", value);
		}
	}

	private double RoundByPrecision(double value) {
		if (FloatingPointPrecision <= 0)
			return Math.round(value);
		return Math.round(value * Math.pow(10, FloatingPointPrecision)) / Math.pow(10, FloatingPointPrecision);
	}

	private double GetValueFromEditText() {
		double natural = GetValueFromText(NaturalNumberEditText().getText().toString(), "");
		double decimal = GetValueFromText(DecimalPartEditText().getText().toString(), "0.");
		double value = natural + decimal;
		value = Math.round(value * Math.pow(10d, FloatingPointPrecision)) / Math.pow(10d, FloatingPointPrecision);
		Log.i(tag, String.format("GetValueFromEditText, natural: %s, decimal: %s, value: %s", natural, decimal, value));
		return value;
	}

	private double GetValueFromText(String valueText, String prefix) {
		if (FarayanUtility.IsNullOrEmpty(valueText))
			valueText = "";
		valueText = FarayanUtility.ConvertNumbersToAscii(valueText);
		valueText = valueText.replaceAll("\\D", "");
		double value = FarayanUtility.TryParseDouble(prefix + valueText, 0);
		return value;
	}

	boolean SkipChangedEvent = false;

	private void SetValueForEditText(double value) {
		String[] parts = FarayanUtility.MoneyFormatted(value, true).split("/");
		if (parts.length > 0) {
			if (!parts[0].equalsIgnoreCase(NaturalNumberEditText().getText().toString())) {
				NaturalNumberEditText().setText(parts[0]);
				NaturalNumberEditText().setSelection(NaturalNumberEditText().getText().length());
			}
		}
		if (parts.length > 1) {
			if (!parts[1].equalsIgnoreCase(DecimalPartEditText().getText().toString())) {
				DecimalPartEditText().setText(parts[1]);
				DecimalPartEditText().setSelection(DecimalPartEditText().getText().length());
			}
		}

		/*String textValue = Double.toString(value);
		long naturalValue = (long) Math.abs(value);
		String naturalValueText = FarayanUtility.MoneyFormatted(naturalValue, true);
		if (!naturalValueText.equalsIgnoreCase(NaturalNumberEditText().getText().toString())) {
			NaturalNumberEditText().setText(naturalValueText);
			NaturalNumberEditText().setSelection(naturalValueText.length());
		}

		if (SupportDecimal && FloatingPointPrecision > 0) {
			String fractionalPartTextRaw = textValue.substring(textValue.indexOf(".") + 1);
			int fractionalPart = FarayanUtility.TryParseInt(fractionalPartTextRaw, 0);
			String fractionalPartText = FarayanUtility.MoneyFormatted(fractionalPart, true);
			String currentFractionalText = DecimalPartEditText().getText().toString();
			if (currentFractionalText.endsWith("0")) {
				int suffixZerosCount = 0;
				int index = currentFractionalText.length() - suffixZerosCount - 1;
				while (index > 0 && currentFractionalText.charAt(index) == '0') {
					suffixZerosCount++;
					index = currentFractionalText.length() - suffixZerosCount - 1;
				}
				fractionalPartText += FarayanUtility.Repeat("0", suffixZerosCount, "");
				fractionalPartText = FarayanUtility.ConvertNumbersToAscii(fractionalPartText);
			}
			if (!fractionalPartText.equalsIgnoreCase(currentFractionalText)) {
				DecimalPartEditText().setText(fractionalPartText);
				DecimalPartEditText().setSelection(DecimalPartEditText().getText().length());
			}
		}*/
	}

	boolean SkipNumberPickerChange = false;

	private void SetValueForNumberPicker(double value) {
		ArrayList<Integer> values = new ArrayList<Integer>();
		long longValue = (long) value;
		while (longValue > 0) {
			values.add((int) (longValue % 1000));
			longValue /= 1000;
		}

		SkipNumberPickerChange = true;
		Level1NumberPicker().setValue(values.size() > 0 ? values.get(0) : 0);
		Level2NumberPicker().setValue(values.size() > 1 ? values.get(1) : 0);
		Level3NumberPicker().setValue(values.size() > 2 ? values.get(2) : 0);
		Level4NumberPicker().setValue(values.size() > 3 ? values.get(3) : 0);

		double decimalPart = value - (long) value;
		Log.i(tag, String.format("decimal part of value (%s) - longValue (%s) is %s", value, (long) value, value - (long) value));
		if (decimalPart > 0 && decimalPart < 1) {
			Log.i(tag, String.format("SetValueForNumberPicker. value (%s) between 0 and 1", value));
			int v = (int) (decimalPart * 1000);
			FloatingPointNumberPicker().setValue(v);
		}
		SkipNumberPickerChange = false;
	}

	public int getDigitsCount() {
		return DigitsCount;
	}

	public void setDigitsCount(int digitsCount) {
		DigitsCount = digitsCount;
		int levels = (int) Math.ceil((double) digitsCount / 3d);
		Level1NumberPicker().setVisibility(levels >= 1 ? View.VISIBLE : View.GONE);
		Level2NumberPicker().setVisibility(levels >= 2 ? View.VISIBLE : View.GONE);
		Level2And1SeparatorTextView().setVisibility(levels >= 2 ? View.VISIBLE : View.GONE);
		Level3NumberPicker().setVisibility(levels >= 3 ? View.VISIBLE : View.GONE);
		Level3And2SeparatorTextView().setVisibility(levels >= 3 ? View.VISIBLE : View.GONE);
		Level4NumberPicker().setVisibility(levels >= 4 ? View.VISIBLE : View.GONE);
		Level4And3SeparatorTextView().setVisibility(levels >= 4 ? View.VISIBLE : View.GONE);
	}

	public void setPrefix(String prefix) {
		PrefixTextView().setText(prefix);
		invalidate();
		requestLayout();
	}

	public void setSuffix(String suffix) {
		SuffixTextView().setText(suffix);
		invalidate();
		requestLayout();
	}

	ICommandEvent<Double> ValueChangedEvent;

	@Override
	public void SetEventHandler(ICommandEvent<Double> iCommandEvent) {
		ValueChangedEvent = iCommandEvent;
	}
}
