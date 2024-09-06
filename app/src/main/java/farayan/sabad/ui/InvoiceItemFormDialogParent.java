package farayan.sabad.ui;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import farayan.commons.FarayanUtility;
import farayan.sabad.R;
import farayan.sabad.ui.Core.SabadBaseDialog;
public abstract class InvoiceItemFormDialogParent extends SabadBaseDialog {

	private static final int layoutID = R.layout.dialog_invoice_item_form;

	protected static final String tag = "InvoiceItemFormDialogParent";


	public InvoiceItemFormDialogParent(@NonNull Activity context) {
		super(context);
		LoadLayout(context);
	}

	public InvoiceItemFormDialogParent(@NonNull Activity context, int themeResId) {
		super(context, themeResId);
		LoadLayout(context);
	}

	public InvoiceItemFormDialogParent(@NonNull Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
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
		long start = System.currentTimeMillis();
		setContentView(layoutID);
		long finish = System.currentTimeMillis();
		Log.i("Performance", String.format("inflating dialog_invoice_item_form takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));

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
		m_GroupPicker = null;
		m_ProductPicker = null;
		m_BarcodeEditText = null;
		m_StartScanButton = null;
		m_StopScanButton = null;
		m_ScanBarcodeView = null;
		m_QuantityNumberEntry = null;
		m_QuantityUnitBox = null;
		m_FeeRialEntry = null;
		m_FeeLastUpdateTextView = null;
		m_RemoveButton = null;
		m_RegisterButton = null;
	}

	private LinearLayout m_TopContainer;
	protected LinearLayout TopContainer(){
		if(m_TopContainer == null)
			m_TopContainer=(LinearLayout)findViewById(R.id.TopContainer);
		return m_TopContainer; 
	}


	private farayan.sabad.models.Group.GroupBox m_GroupPicker;
	protected farayan.sabad.models.Group.GroupBox GroupPicker(){
		if(m_GroupPicker == null)
			m_GroupPicker=(farayan.sabad.models.Group.GroupBox)findViewById(R.id.GroupPicker);
		return m_GroupPicker; 
	}


	private farayan.sabad.models.Product.ProductBox m_ProductPicker;
	protected farayan.sabad.models.Product.ProductBox ProductPicker(){
		if(m_ProductPicker == null)
			m_ProductPicker=(farayan.sabad.models.Product.ProductBox)findViewById(R.id.ProductPicker);
		return m_ProductPicker; 
	}


	private farayan.commons.UI.CommonTextInputEditText m_BarcodeEditText;
	protected farayan.commons.UI.CommonTextInputEditText BarcodeEditText(){
		if(m_BarcodeEditText == null)
			m_BarcodeEditText=(farayan.commons.UI.CommonTextInputEditText)findViewById(R.id.BarcodeEditText);
		return m_BarcodeEditText; 
	}


	private com.google.android.material.button.MaterialButton m_StartScanButton;
	protected com.google.android.material.button.MaterialButton StartScanButton(){
		if(m_StartScanButton == null)
			m_StartScanButton=(com.google.android.material.button.MaterialButton)findViewById(R.id.StartScanButton);
		return m_StartScanButton; 
	}


	private com.google.android.material.button.MaterialButton m_StopScanButton;
	protected com.google.android.material.button.MaterialButton StopScanButton(){
		if(m_StopScanButton == null)
			m_StopScanButton=(com.google.android.material.button.MaterialButton)findViewById(R.id.StopScanButton);
		return m_StopScanButton; 
	}


	private com.journeyapps.barcodescanner.BarcodeView m_ScanBarcodeView;
	protected com.journeyapps.barcodescanner.BarcodeView ScanBarcodeView(){
		if(m_ScanBarcodeView == null)
			m_ScanBarcodeView=(com.journeyapps.barcodescanner.BarcodeView)findViewById(R.id.ScanBarcodeView);
		return m_ScanBarcodeView; 
	}


	private farayan.commons.components.NumberEntry m_QuantityNumberEntry;
	protected farayan.commons.components.NumberEntry QuantityNumberEntry(){
		if(m_QuantityNumberEntry == null)
			m_QuantityNumberEntry=(farayan.commons.components.NumberEntry)findViewById(R.id.QuantityNumberEntry);
		return m_QuantityNumberEntry; 
	}


	private farayan.sabad.models.Unit.UnitBox m_QuantityUnitBox;
	protected farayan.sabad.models.Unit.UnitBox QuantityUnitBox(){
		if(m_QuantityUnitBox == null)
			m_QuantityUnitBox=(farayan.sabad.models.Unit.UnitBox)findViewById(R.id.QuantityUnitBox);
		return m_QuantityUnitBox; 
	}


	private farayan.commons.UI.RialEntryComponent m_FeeRialEntry;
	protected farayan.commons.UI.RialEntryComponent FeeRialEntry(){
		if(m_FeeRialEntry == null)
			m_FeeRialEntry=(farayan.commons.UI.RialEntryComponent)findViewById(R.id.FeeRialEntry);
		return m_FeeRialEntry; 
	}


	private TextView m_FeeLastUpdateTextView;
	protected TextView FeeLastUpdateTextView(){
		if(m_FeeLastUpdateTextView == null)
			m_FeeLastUpdateTextView=(TextView)findViewById(R.id.FeeLastUpdateTextView);
		return m_FeeLastUpdateTextView; 
	}


	private com.google.android.material.button.MaterialButton m_RemoveButton;
	protected com.google.android.material.button.MaterialButton RemoveButton(){
		if(m_RemoveButton == null)
			m_RemoveButton=(com.google.android.material.button.MaterialButton)findViewById(R.id.RemoveButton);
		return m_RemoveButton; 
	}


	private com.google.android.material.button.MaterialButton m_RegisterButton;
	protected com.google.android.material.button.MaterialButton RegisterButton(){
		if(m_RegisterButton == null)
			m_RegisterButton=(com.google.android.material.button.MaterialButton)findViewById(R.id.RegisterButton);
		return m_RegisterButton; 
	}

	protected void InitializeLayout(){}
}
