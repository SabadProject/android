package farayan.sabad.ui;

import android.os.Bundle;
import android.util.Log;

import farayan.commons.FarayanUtility;
import farayan.sabad.R;
import farayan.sabad.ui.Core.SabadBaseActivity;
public abstract class ScanActivityParent extends SabadBaseActivity {
	protected static final String tag = "ScanActivityParent";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NullLoadedViews();
		long start = System.currentTimeMillis();
		setContentView(R.layout.activity_scan);
		long finish = System.currentTimeMillis();
		Log.i("Performance", String.format("inflating activity_scan takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));
		FarayanUtility.OverrideFonts(findViewById(android.R.id.content) ,getDefaultFont());
		InitializeLayout();	}

	private void NullLoadedViews() {
		m_ScanBarcodeView = null;
		m_PurchaseSummary = null;
	}

	private com.journeyapps.barcodescanner.BarcodeView m_ScanBarcodeView;
	protected com.journeyapps.barcodescanner.BarcodeView ScanBarcodeView(){
		if(m_ScanBarcodeView == null)
			m_ScanBarcodeView = (com.journeyapps.barcodescanner.BarcodeView)findViewById(R.id.ScanBarcodeView);
		return m_ScanBarcodeView; 
	}

	private farayan.sabad.ui.PurchaseSummaryComponent m_PurchaseSummary;
	protected farayan.sabad.ui.PurchaseSummaryComponent PurchaseSummary(){
		if(m_PurchaseSummary == null)
			m_PurchaseSummary = (farayan.sabad.ui.PurchaseSummaryComponent)findViewById(R.id.PurchaseSummary);
		return m_PurchaseSummary; 
	}

	protected void InitializeLayout(){}
}
