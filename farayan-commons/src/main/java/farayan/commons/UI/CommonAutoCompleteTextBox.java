package farayan.commons.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;

import farayan.commons.FarayanUtility;
import farayan.commons.R;
import farayan.commons.UI.Commons.TextInputStates;

public abstract class CommonAutoCompleteTextBox extends androidx.appcompat.widget.AppCompatAutoCompleteTextView {
	private int hintGravity;
	private int textGravity;
	private Drawable errorRightDrawable;
	private Drawable normalRightDrawable;
	private Drawable errorLeftDrawable;
	private Drawable normalLeftDrawable;
	private int errorDrawableTintColor = Color.RED;

	public CommonAutoCompleteTextBox(Context context) {
		super(context);
	}

	public CommonAutoCompleteTextBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		LoadAttributes(attrs);
		InitializeLayoutPrivate();
	}

	public CommonAutoCompleteTextBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LoadAttributes(attrs);
		InitializeLayoutPrivate();
	}

	boolean AutoOpenOnClick;

	private void LoadAttributes(AttributeSet attrs) {
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CommonAutoCompleteTextBox, 0, 0);
		try {
			AutoOpenOnClick = typedArray.getBoolean(R.styleable.CommonAutoCompleteTextBox_autoOpenOnClick, false);
			hintGravity = typedArray.getInteger(R.styleable.CommonTextInputEditText_hintGravity, -1);
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
			errorLeftDrawable = typedArray.getDrawable(R.styleable.CommonAutoCompleteTextBox_errorLeftDrawable);
			errorRightDrawable = typedArray.getDrawable(R.styleable.CommonAutoCompleteTextBox_errorRightDrawable);
		} finally {
			typedArray.recycle();
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

	Handler handler = new Handler();
	Runnable showDropDownRunnable = new Runnable() {

		@Override
		public void run() {
			showDropDown();
		}
	};

	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
		if (focused) {
			handler.postDelayed(showDropDownRunnable, 500);
		}
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}

	@Override
	public boolean enoughToFilter() {
		return true;
	}

	protected abstract void Reload();

	private void InitializeLayoutPrivate() {
		if (AutoOpenOnClick) {
			this.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					CommonAutoCompleteTextBox.this.showDropDown();
				}
			});
		}
		InitializeLayout();
	}

	@Override
	protected CharSequence convertSelectionToString(Object selectedItem) {
		return textValue(selectedItem);
	}

	protected abstract CharSequence textValue(Object selectedItem);

	protected abstract void InitializeLayout();
}