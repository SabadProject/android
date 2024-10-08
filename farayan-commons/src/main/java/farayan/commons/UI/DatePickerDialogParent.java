package farayan.commons.UI;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import farayan.commons.FarayanUtility;
import farayan.commons.R;
import farayan.commons.UI.Core.FarayanCommonBaseDialog;
public abstract class DatePickerDialogParent extends FarayanCommonBaseDialog {

	private static final int layoutID = R.layout.dialog_date_picker;

	protected static final String tag = "DatePickerDialogParent";


	public DatePickerDialogParent(@NonNull Activity context) {
		super(context);
		LoadLayout(context);
	}

	public DatePickerDialogParent(@NonNull Activity context, int themeResId) {
		super(context, themeResId);
		LoadLayout(context);
	}

	public DatePickerDialogParent(@NonNull Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
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
		Log.i("Performance", String.format("inflating dialog_date_picker takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));

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
		m_HeaderTextView = null;
		m_DatePicker = null;
		m_SelectDateButton = null;
	}

	private TextView m_HeaderTextView;
	protected TextView HeaderTextView(){
		if(m_HeaderTextView == null)
			m_HeaderTextView=(TextView)findViewById(R.id.HeaderTextView);
		return m_HeaderTextView; 
	}


	private farayan.commons.UI.DateTimePickerComponent m_DatePicker;
	protected farayan.commons.UI.DateTimePickerComponent DatePicker(){
		if(m_DatePicker == null)
			m_DatePicker=(farayan.commons.UI.DateTimePickerComponent)findViewById(R.id.DatePicker);
		return m_DatePicker; 
	}


	private Button m_SelectDateButton;
	protected Button SelectDateButton(){
		if(m_SelectDateButton == null)
			m_SelectDateButton=(Button)findViewById(R.id.SelectDateButton);
		return m_SelectDateButton; 
	}

	protected void InitializeLayout(){}
}
