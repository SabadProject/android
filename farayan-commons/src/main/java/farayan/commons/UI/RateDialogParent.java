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
public abstract class RateDialogParent extends FarayanCommonBaseDialog {

	private static final int layoutID = R.layout.dialog_rate;

	protected static final String tag = "RateDialogParent";


	public RateDialogParent(@NonNull Activity context) {
		super(context);
		LoadLayout(context);
	}

	public RateDialogParent(@NonNull Activity context, int themeResId) {
		super(context, themeResId);
		LoadLayout(context);
	}

	public RateDialogParent(@NonNull Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
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
		Log.i("Performance", String.format("inflating dialog_rate takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));

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
		m_RateCommentTextView = null;
		m_RateImageButton = null;
		m_NotNowImageButton = null;
	}

	private TextView m_RateCommentTextView;
	protected TextView RateCommentTextView(){
		if(m_RateCommentTextView == null)
			m_RateCommentTextView=(TextView)findViewById(R.id.RateCommentTextView);
		return m_RateCommentTextView; 
	}


	private ImageButton m_RateImageButton;
	protected ImageButton RateImageButton(){
		if(m_RateImageButton == null)
			m_RateImageButton=(ImageButton)findViewById(R.id.RateImageButton);
		return m_RateImageButton; 
	}


	private ImageButton m_NotNowImageButton;
	protected ImageButton NotNowImageButton(){
		if(m_NotNowImageButton == null)
			m_NotNowImageButton=(ImageButton)findViewById(R.id.NotNowImageButton);
		return m_NotNowImageButton; 
	}

	protected void InitializeLayout(){}
}
