package farayan.sabad.ui;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import farayan.commons.FarayanUtility;
import farayan.sabad.R;
import farayan.sabad.ui.Core.SabadBaseDialog;
public abstract class CheckoutPurchaseDialogParent extends SabadBaseDialog {

	private static final int layoutID = R.layout.dialog_checkout_purchase;

	protected static final String tag = "CheckoutPurchaseDialogParent";


	public CheckoutPurchaseDialogParent(@NonNull Activity context) {
		super(context);
		LoadLayout(context);
	}

	public CheckoutPurchaseDialogParent(@NonNull Activity context, int themeResId) {
		super(context, themeResId);
		LoadLayout(context);
	}

	public CheckoutPurchaseDialogParent(@NonNull Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
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
		Log.i("Performance", String.format("inflating dialog_checkout_purchase takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));

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
		m_TopContainer = null;
		m_PurchasableCountEditText = null;
		m_PickedCountEditText = null;
		m_RemainedCountEditText = null;
		m_PayableEditText = null;
		m_StorePicker = null;
		m_PurchasedDatePicker = null;
		m_CheckoutButton = null;
	}

	private LinearLayout m_TopContainer;
	protected LinearLayout TopContainer(){
		if(m_TopContainer == null)
			m_TopContainer=(LinearLayout)findViewById(R.id.TopContainer);
		return m_TopContainer; 
	}


	private farayan.commons.UI.CommonTextInputEditText m_PurchasableCountEditText;
	protected farayan.commons.UI.CommonTextInputEditText PurchasableCountEditText(){
		if(m_PurchasableCountEditText == null)
			m_PurchasableCountEditText=(farayan.commons.UI.CommonTextInputEditText)findViewById(R.id.PurchasableCountEditText);
		return m_PurchasableCountEditText; 
	}


	private farayan.commons.UI.CommonTextInputEditText m_PickedCountEditText;
	protected farayan.commons.UI.CommonTextInputEditText PickedCountEditText(){
		if(m_PickedCountEditText == null)
			m_PickedCountEditText=(farayan.commons.UI.CommonTextInputEditText)findViewById(R.id.PickedCountEditText);
		return m_PickedCountEditText; 
	}


	private farayan.commons.UI.CommonTextInputEditText m_RemainedCountEditText;
	protected farayan.commons.UI.CommonTextInputEditText RemainedCountEditText(){
		if(m_RemainedCountEditText == null)
			m_RemainedCountEditText=(farayan.commons.UI.CommonTextInputEditText)findViewById(R.id.RemainedCountEditText);
		return m_RemainedCountEditText; 
	}


	private farayan.commons.UI.CommonTextInputEditText m_PayableEditText;
	protected farayan.commons.UI.CommonTextInputEditText PayableEditText(){
		if(m_PayableEditText == null)
			m_PayableEditText=(farayan.commons.UI.CommonTextInputEditText)findViewById(R.id.PayableEditText);
		return m_PayableEditText; 
	}


	private farayan.sabad.models.Store.StoreBox m_StorePicker;
	protected farayan.sabad.models.Store.StoreBox StorePicker(){
		if(m_StorePicker == null)
			m_StorePicker=(farayan.sabad.models.Store.StoreBox)findViewById(R.id.StorePicker);
		return m_StorePicker; 
	}


	private farayan.commons.UI.DateTimeBox m_PurchasedDatePicker;
	protected farayan.commons.UI.DateTimeBox PurchasedDatePicker(){
		if(m_PurchasedDatePicker == null)
			m_PurchasedDatePicker=(farayan.commons.UI.DateTimeBox)findViewById(R.id.PurchasedDatePicker);
		return m_PurchasedDatePicker; 
	}


	private com.google.android.material.button.MaterialButton m_CheckoutButton;
	protected com.google.android.material.button.MaterialButton CheckoutButton(){
		if(m_CheckoutButton == null)
			m_CheckoutButton=(com.google.android.material.button.MaterialButton)findViewById(R.id.CheckoutButton);
		return m_CheckoutButton; 
	}

	protected void InitializeLayout(){}
}
