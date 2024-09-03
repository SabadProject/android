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
public abstract class QuestionDialogParent extends FarayanCommonBaseDialog {

	private static final int layoutID = R.layout.dialog_question;

	protected static final String tag = "QuestionDialogParent";


	public QuestionDialogParent(@NonNull Activity context) {
		super(context);
		LoadLayout(context);
	}

	public QuestionDialogParent(@NonNull Activity context, int themeResId) {
		super(context, themeResId);
		LoadLayout(context);
	}

	public QuestionDialogParent(@NonNull Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
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
		Log.i("Performance", String.format("inflating dialog_question takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));

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
		m_RootLayout = null;
		m_HeaderLayout = null;
		m_HeaderTextView = null;
		m_QuestionTextView = null;
		m_FooterLayout = null;
		m_StartButton = null;
		m_MiddleButton = null;
		m_LastButton = null;
	}

	private LinearLayout m_RootLayout;
	protected LinearLayout RootLayout(){
		if(m_RootLayout == null)
			m_RootLayout=(LinearLayout)findViewById(R.id.RootLayout);
		return m_RootLayout; 
	}


	private LinearLayout m_HeaderLayout;
	protected LinearLayout HeaderLayout(){
		if(m_HeaderLayout == null)
			m_HeaderLayout=(LinearLayout)findViewById(R.id.HeaderLayout);
		return m_HeaderLayout; 
	}


	private TextView m_HeaderTextView;
	protected TextView HeaderTextView(){
		if(m_HeaderTextView == null)
			m_HeaderTextView=(TextView)findViewById(R.id.HeaderTextView);
		return m_HeaderTextView; 
	}


	private TextView m_QuestionTextView;
	protected TextView QuestionTextView(){
		if(m_QuestionTextView == null)
			m_QuestionTextView=(TextView)findViewById(R.id.QuestionTextView);
		return m_QuestionTextView; 
	}


	private LinearLayout m_FooterLayout;
	protected LinearLayout FooterLayout(){
		if(m_FooterLayout == null)
			m_FooterLayout=(LinearLayout)findViewById(R.id.FooterLayout);
		return m_FooterLayout; 
	}


	private Button m_StartButton;
	protected Button StartButton(){
		if(m_StartButton == null)
			m_StartButton=(Button)findViewById(R.id.StartButton);
		return m_StartButton; 
	}


	private Button m_MiddleButton;
	protected Button MiddleButton(){
		if(m_MiddleButton == null)
			m_MiddleButton=(Button)findViewById(R.id.MiddleButton);
		return m_MiddleButton; 
	}


	private Button m_LastButton;
	protected Button LastButton(){
		if(m_LastButton == null)
			m_LastButton=(Button)findViewById(R.id.LastButton);
		return m_LastButton; 
	}

	protected void InitializeLayout(){}
}
