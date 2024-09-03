package farayan.commons.UI;
import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.util.*;
import androidx.annotation.*;
import android.util.*;
import farayan.commons.FarayanUtility;
import farayan.commons.*;
import farayan.commons.UI.Core.*;
public abstract class TimeDialogParent extends FarayanCommonBaseDialog {

	private static final int layoutID = R.layout.dialog_time;

	protected static final String tag = "TimeDialogParent";


	public TimeDialogParent(@NonNull Activity context) {
		super(context);
		LoadLayout(context);
	}

	public TimeDialogParent(@NonNull Activity context, int themeResId) {
		super(context, themeResId);
		LoadLayout(context);
	}

	public TimeDialogParent(@NonNull Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
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
		Log.i("Performance", String.format("inflating dialog_time takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));

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
		m_Picker = null;
		m_SelectButton = null;
	}

	private TextView m_HeaderTextView;
	protected TextView HeaderTextView(){
		if(m_HeaderTextView == null)
			m_HeaderTextView=(TextView)findViewById(R.id.HeaderTextView);
		return m_HeaderTextView; 
	}


	private farayan.commons.UI.TimePickerComponent m_Picker;
	protected farayan.commons.UI.TimePickerComponent Picker(){
		if(m_Picker == null)
			m_Picker=(farayan.commons.UI.TimePickerComponent)findViewById(R.id.Picker);
		return m_Picker; 
	}


	private Button m_SelectButton;
	protected Button SelectButton(){
		if(m_SelectButton == null)
			m_SelectButton=(Button)findViewById(R.id.SelectButton);
		return m_SelectButton; 
	}

	protected void InitializeLayout(){}
}
