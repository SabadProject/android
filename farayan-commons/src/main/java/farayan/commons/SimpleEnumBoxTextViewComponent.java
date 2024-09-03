package farayan.commons;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import farayan.commons.UI.Views.IEnumBoxTextView;

public class SimpleEnumBoxTextViewComponent extends TextView implements IEnumBoxTextView {
	private final Context Ctx;

	public SimpleEnumBoxTextViewComponent(Context context) {
		super(context);
		this.Ctx = context;
		InitializeComponents();
	}

	public SimpleEnumBoxTextViewComponent(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.Ctx = context;
		InitializeComponents();
	}

	public SimpleEnumBoxTextViewComponent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.Ctx = context;
		InitializeComponents();
	}

	public SimpleEnumBoxTextViewComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		this.Ctx = context;
		InitializeComponents();
	}

	protected void InitializeComponents() {
		setIncludeFontPadding(false);
		setTypeface(getTypeface());
		int padding = (int) getResources().getDimension(R.dimen.simpleTextViewPadding);
		setPadding(padding, padding, padding, padding);
	}

	@Override
	public Typeface getTypeface() {
		return FarayanBaseApp.getFont();
	}

}
