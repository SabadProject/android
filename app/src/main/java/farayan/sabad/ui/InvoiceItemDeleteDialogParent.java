package farayan.sabad.ui;
import android.app.*;
import android.view.*;
import android.util.*;
import androidx.annotation.*;
import farayan.commons.FarayanUtility;
import farayan.sabad.R;
import farayan.sabad.ui.Core.*;
public abstract class InvoiceItemDeleteDialogParent extends SabadBaseDialog {

	private static final int layoutID = R.layout.dialog_group_form;

	protected static final String tag = "InvoiceItemDeleteDialogParent";


	public InvoiceItemDeleteDialogParent(@NonNull Activity context) {
		super(context);
		LoadLayout(context);
	}

	public InvoiceItemDeleteDialogParent(@NonNull Activity context, int themeResId) {
		super(context, themeResId);
		LoadLayout(context);
	}

	public InvoiceItemDeleteDialogParent(@NonNull Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		LoadLayout(context);
	}

	private void LoadLayout(Activity activity) {
		dialogPreLoadLayout(activity);
		if (FarayanUtility.IsUsable(dialogTitle())) {
			setTitle(dialogTitle());
		} else {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
		NullLoadedViews();
		long start = System.currentTimeMillis();
		setContentView(layoutID);
		long finish = System.currentTimeMillis();
		Log.i("Performance", String.format("inflating dialog_invoice_item_delete takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));

		if (dialogFullScreen()) {
			Window window = getWindow();
			WindowManager.LayoutParams wlp = window.getAttributes();
			wlp.gravity = Gravity.CENTER;
			window.setAttributes(wlp);
			getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
		}

		if (dialogResizeByKeyboard()) {
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		}

		FarayanUtility.OverrideFonts(findViewById(android.R.id.content), getDefaultFont());
		InitializeLayout();
	}

	private void NullLoadedViews() {
	}
	protected void InitializeLayout(){}
}
