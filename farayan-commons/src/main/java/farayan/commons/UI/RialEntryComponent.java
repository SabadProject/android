package farayan.commons.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import farayan.commons.Commons.Rial;
import farayan.commons.R;

public class RialEntryComponent extends RialEntryComponentParent
{
	private int hintGravity;
	private int textGravity;
	private int unitGravity;

	public RialEntryComponent(Context context) {
		super(context);
	}

	public RialEntryComponent(Context context, AttributeSet attrs) {
		super(context, attrs);
		LoadAttributes(attrs, 0);
	}

	public RialEntryComponent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LoadAttributes(attrs, defStyle);
	}

	public RialEntryComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		LoadAttributes(attrs, defStyleRes);
	}

	private void LoadAttributes(AttributeSet attrs, int defStyle) {
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RialEntryComponent, defStyle, 0);
		try {
			String hint = typedArray.getString(R.styleable.RialEntryComponent_hint);
			DisplayableNumberEntry().setHint(hint);
			textGravity = getGravity();
			hintGravity = typedArray.getInt(R.styleable.RialEntryComponent_hintGravity, textGravity);
			unitGravity = typedArray.getInt(R.styleable.RialEntryComponent_unitGravity, textGravity);
			CoefficientBox().Reload(unitGravity);
			applyGravity("");
		} finally {
			typedArray.recycle();
		}
	}

	private void applyGravity(CharSequence s) {
		if (textGravity == hintGravity)
			return;
		if (s.length() > 0) {
			DisplayableNumberEntry().setGravity(textGravity);
		} else {
			DisplayableNumberEntry().setGravity(hintGravity);
		}
	}

	public void setValue(Rial rial) {
		DisplayableNumberEntry().setDoubleValue(rial.Displayable);
		CoefficientBox().setSelection(rial.Coefficient.ordinal());
	}

	public Rial getValue() {
		double typed = DisplayableNumberEntry().getDoubleValue();
		Rial.Coefficients coefficient = CoefficientBox().getValue();
		return new Rial(typed, coefficient);
	}

	@Override
	protected void InitializeComponents() {
		DisplayableNumberEntry().addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				applyGravity(charSequence);
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});
	}
}
