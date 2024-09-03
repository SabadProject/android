package farayan.commons.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.AttributeSet;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import farayan.commons.FarayanUtility;
import farayan.commons.R;
import farayan.commons.UI.Commons.CustomTypefaceSpan;
import farayan.commons.UI.Commons.IErrorableView;
import farayan.commons.UI.Commons.TextInputStates;

public class CommonTextInputLayout extends TextInputLayout implements IErrorableView
{
	public CommonTextInputLayout(Context context) {
		super(context, null);
	}

	public CommonTextInputLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CommonTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CommonTextInputLayout, defStyleAttr, 0);
		try {
			errorBackground = typedArray.getDrawable(R.styleable.CommonTextInputLayout_errorBackground);
		} finally {
			typedArray.recycle();
		}
	}

	Drawable errorBackground;
	Drawable defaultBackground;

	private TextInputStates State;

	@Override
	public void setError(@Nullable CharSequence error) {
		Objects.requireNonNull(getEditText()).setError(error);

		if (FarayanUtility.IsUsable(error)) {
			SpannableStringBuilder errorSpanned = new SpannableStringBuilder(error);
			errorSpanned.setSpan(new CustomTypefaceSpan(getEditText().getTypeface()), 0, error.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			super.setError(errorSpanned);

			if (State == TextInputStates.Error)
				return;
			State = TextInputStates.Error;
			defaultBackground = getBackground();
			setBackground(errorBackground);
		} else {
			super.setError("");

			if (State == TextInputStates.Normal)
				return;
			State = TextInputStates.Normal;
			setBackground(defaultBackground);
		}
	}

}
