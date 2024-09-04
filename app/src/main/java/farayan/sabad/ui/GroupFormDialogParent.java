package farayan.sabad.ui;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import farayan.commons.FarayanUtility;
import farayan.sabad.R;
import farayan.sabad.ui.Core.SabadBaseDialog;
public abstract class GroupFormDialogParent extends SabadBaseDialog {

	private static final int layoutID = R.layout.dialog_group_form;

	protected static final String tag = "GroupFormDialogParent";


	public GroupFormDialogParent(@NonNull Activity context) {
		super(context);
		LoadLayout(context);
	}

	public GroupFormDialogParent(@NonNull Activity context, int themeResId) {
		super(context, themeResId);
		LoadLayout(context);
	}

	public GroupFormDialogParent(@NonNull Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
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
		Log.i("Performance", String.format("inflating dialog_group_form takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));

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
		m_NameTextLayout = null;
		m_NameEditText = null;
		m_PersistButton = null;
		m_RemoveButton = null;
	}

	private farayan.commons.UI.CommonTextInputLayout m_NameTextLayout;
	protected farayan.commons.UI.CommonTextInputLayout NameTextLayout(){
		if(m_NameTextLayout == null)
			m_NameTextLayout=(farayan.commons.UI.CommonTextInputLayout)findViewById(R.id.NameTextLayout);
		return m_NameTextLayout; 
	}


	private farayan.commons.UI.CommonTextInputEditText m_NameEditText;
	protected farayan.commons.UI.CommonTextInputEditText NameEditText(){
		if(m_NameEditText == null)
			m_NameEditText=(farayan.commons.UI.CommonTextInputEditText)findViewById(R.id.NameEditText);
		return m_NameEditText; 
	}


	private com.google.android.material.button.MaterialButton m_PersistButton;
	protected com.google.android.material.button.MaterialButton PersistButton(){
		if(m_PersistButton == null)
			m_PersistButton=(com.google.android.material.button.MaterialButton)findViewById(R.id.PersistButton);
		return m_PersistButton; 
	}


	private com.google.android.material.button.MaterialButton m_RemoveButton;
	protected com.google.android.material.button.MaterialButton RemoveButton(){
		if(m_RemoveButton == null)
			m_RemoveButton=(com.google.android.material.button.MaterialButton)findViewById(R.id.RemoveButton);
		return m_RemoveButton; 
	}

	protected void InitializeLayout(){}
}
