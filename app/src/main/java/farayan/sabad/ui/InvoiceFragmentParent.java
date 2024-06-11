package farayan.sabad.ui;
import android.os.Bundle;
import android.view.*;
import android.util.*;

import farayan.commons.FarayanUtility;
import farayan.sabad.R;
import farayan.sabad.ui.Core.*;
public abstract class InvoiceFragmentParent extends SabadBaseFragment {
	private View rootView = null;

	protected static final String tag = "InvoiceFragmentParent";


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		NullLoadedViews();
		long start = System.currentTimeMillis();
		rootView = inflater.inflate(R.layout.fragment_invoice, container, false);
		long finish = System.currentTimeMillis();
		Log.i("Performance", String.format("inflating fragment_invoice takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));
		FarayanUtility.OverrideFonts(rootView,getDefaultFont());
		InitializeLayout();
		return rootView;
	}

	private void NullLoadedViews() {
		m_SummaryInvoicesItem = null;
		m_InvoiceItemsRecyclerView = null;
	}

	private farayan.sabad.ui.InvoicesItemComponent m_SummaryInvoicesItem = null;

	protected farayan.sabad.ui.InvoicesItemComponent SummaryInvoicesItem(){
		if(m_SummaryInvoicesItem == null && rootView != null)
			m_SummaryInvoicesItem = (farayan.sabad.ui.InvoicesItemComponent)rootView.findViewById(R.id.SummaryInvoicesItem);
		return m_SummaryInvoicesItem; 
	}

	private androidx.recyclerview.widget.RecyclerView m_InvoiceItemsRecyclerView = null;

	protected androidx.recyclerview.widget.RecyclerView InvoiceItemsRecyclerView(){
		if(m_InvoiceItemsRecyclerView == null && rootView != null)
			m_InvoiceItemsRecyclerView = (androidx.recyclerview.widget.RecyclerView)rootView.findViewById(R.id.InvoiceItemsRecyclerView);
		return m_InvoiceItemsRecyclerView; 
	}

	protected void InitializeLayout(){}
}
