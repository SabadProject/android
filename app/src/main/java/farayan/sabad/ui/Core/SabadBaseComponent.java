package farayan.sabad.ui.Core;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import farayan.commons.UI.Core.FarayanCommonBaseComponent;

public abstract class SabadBaseComponent extends FarayanCommonBaseComponent
{
	public SabadBaseComponent(Context context) {
		super(context);
	}

	public SabadBaseComponent(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SabadBaseComponent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SabadBaseComponent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	protected Typeface getDefaultFont() {
		return null;
	}
}
