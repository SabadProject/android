package farayan.commons.UI.Core;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;

import androidx.annotation.NonNull;

import farayan.commons.FarayanBaseApp;

/**
 * Created by Homayoun on 24/11/2016.
 */

public class FarayanCommonBaseDialog extends Dialog
{
	protected final Activity TheActivity;

	public FarayanCommonBaseDialog(@NonNull Activity context) {
		super(context);
		TheActivity = context;
	}

	public FarayanCommonBaseDialog(@NonNull Activity context, int theme) {
		super(context, theme);
		TheActivity = context;
	}

	protected FarayanCommonBaseDialog(@NonNull Activity context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		TheActivity = context;
	}

	protected boolean dialogResizeByKeyboard() {
		return true;
	}

	protected boolean dialogFullScreen() {
		return true;
	}

	protected CharSequence dialogTitle() {
		return null;
	}

	protected void dialogPreLoadLayout(Activity activity) {
	}

	protected Typeface getDefaultFont() {
		return FarayanBaseApp.getFont();
	}
}
