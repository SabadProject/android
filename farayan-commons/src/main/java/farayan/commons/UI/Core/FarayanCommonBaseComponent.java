package farayan.commons.UI.Core;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import farayan.commons.FarayanBaseApp;

public abstract class FarayanCommonBaseComponent extends LinearLayout
{

	public FarayanCommonBaseComponent(Context context, @NonNull ViewGroup parent, boolean attachToParent) {
		super(context);
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LoadViewBinding(layoutInflater, parent, attachToParent);
	}

	public FarayanCommonBaseComponent(Context context) {
		super(context);
	}

	public FarayanCommonBaseComponent(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FarayanCommonBaseComponent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FarayanCommonBaseComponent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	protected Typeface getDefaultFont() {
		return FarayanBaseApp.getFont();
	}

	protected void LoadViewBinding(LayoutInflater layoutInflater, ViewGroup parent, boolean attachToParent) {
	}
}
