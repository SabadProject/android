package farayan.commons.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.google.android.material.textfield.TextInputEditText;

import farayan.commons.FarayanUtility;
import farayan.commons.R;
import farayan.commons.UI.Commons.TextInputStates;

public class CommonTextInputEditText extends TextInputEditText
{
	private int hintGravity;
	private int textGravity;
	private Drawable errorRightDrawable;
	private Drawable normalRightDrawable;
	private Drawable errorLeftDrawable;
	private Drawable normalLeftDrawable;
	private int errorDrawableTintColor = Color.RED;

	public CommonTextInputEditText(Context context) {
		super(context);

		InitializeComponent();
	}

	public CommonTextInputEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		LoadAttributes(attrs);
	}

	public CommonTextInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		LoadAttributes(attrs);
	}

	private void LoadAttributes(AttributeSet attrs) {
		TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.CommonTextInputEditText, 0, 0);
		try {
			hintGravity = attributes.getInteger(R.styleable.CommonTextInputEditText_hintGravity, -1);
			textGravity = getGravity();
			if (hintGravity != -1) {
				addTextChangedListener(new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {

					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						applyGravity(s);
					}

					@Override
					public void afterTextChanged(Editable s) {

					}
				});
			}
			applyGravity(getText());
			errorLeftDrawable = attributes.getDrawable(R.styleable.CommonTextInputEditText_errorLeftDrawable);
			errorRightDrawable = attributes.getDrawable(R.styleable.CommonTextInputEditText_errorRightDrawable);
		} finally {
			attributes.recycle();
		}
	}

	private void applyGravity(CharSequence s) {
		if (s.length() > 0) {
			setGravity(textGravity);
		} else {
			setGravity(hintGravity);
		}
	}

	public void setErrorLeftDrawable(Drawable errorLeftDrawable) {
		this.errorLeftDrawable = errorLeftDrawable;
	}

	public void setErrorRightDrawable(Drawable errorRightDrawable) {
		this.errorRightDrawable = errorRightDrawable;
	}

	private TextInputStates State = TextInputStates.Normal;

	@Override
	public void setError(CharSequence error) {
		boolean hasError = error != null && FarayanUtility.IsUsable(error.toString());
		Drawable[] drawables = getCompoundDrawables();

		if (hasError) {
			if (State == TextInputStates.Error)
				return;
			State = TextInputStates.Error;
			normalLeftDrawable = drawables[0];
			normalRightDrawable = drawables[2];

			if (errorLeftDrawable != null)
				errorLeftDrawable.setColorFilter(errorDrawableTintColor, PorterDuff.Mode.DST);
			if (errorRightDrawable != null)
				errorRightDrawable.setColorFilter(errorDrawableTintColor, PorterDuff.Mode.DST);

			Drawable finalLeftDrawable = errorLeftDrawable == null ? normalLeftDrawable : errorLeftDrawable;
			Drawable finalRightDrawable = errorRightDrawable == null ? normalRightDrawable : errorRightDrawable;

			setCompoundDrawablesWithIntrinsicBounds(finalLeftDrawable, drawables[1], finalRightDrawable, drawables[3]);
		} else {
			if (State == TextInputStates.Normal)
				return;
			State = TextInputStates.Normal;
			setCompoundDrawablesWithIntrinsicBounds(normalLeftDrawable, drawables[1], normalRightDrawable, drawables[3]);
		}
	}

	private void InitializeComponent() {
	}
}
