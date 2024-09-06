package farayan.sabad.ui.Core;


import android.app.Activity;

import androidx.annotation.NonNull;

import farayan.commons.UI.Core.FarayanCommonBaseDialog;

public abstract class SabadBaseDialog extends FarayanCommonBaseDialog
{
	public SabadBaseDialog(@NonNull Activity context) {
		super(context);
	}

	public SabadBaseDialog(@NonNull Activity context, int theme) {
		super(context, theme);
	}

	protected SabadBaseDialog(@NonNull Activity context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}
}
