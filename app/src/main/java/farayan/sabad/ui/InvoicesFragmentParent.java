package farayan.sabad.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import farayan.commons.FarayanUtility;
import farayan.sabad.R;
import farayan.sabad.ui.Core.SabadBaseFragment;
public abstract class InvoicesFragmentParent extends SabadBaseFragment {
	private View rootView = null;

	protected static final String tag = "InvoicesFragmentParent";


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		NullLoadedViews();
		long start = System.currentTimeMillis();
		rootView = inflater.inflate(R.layout.fragment_invoices, container, false);
		long finish = System.currentTimeMillis();
		Log.i("Performance", String.format("inflating fragment_invoices takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));
		FarayanUtility.OverrideFonts(rootView,getDefaultFont());
		InitializeLayout();
		return rootView;
	}

	private void NullLoadedViews() {
		m_InvoicesRecyclerView = null;
		m_EmptyTextView = null;
	}

	private androidx.recyclerview.widget.RecyclerView m_InvoicesRecyclerView = null;

	protected androidx.recyclerview.widget.RecyclerView InvoicesRecyclerView(){
		if(m_InvoicesRecyclerView == null && rootView != null)
			m_InvoicesRecyclerView = (androidx.recyclerview.widget.RecyclerView)rootView.findViewById(R.id.InvoicesRecyclerView);
		return m_InvoicesRecyclerView; 
	}

	private TextView m_EmptyTextView = null;

	protected TextView EmptyTextView(){
		if(m_EmptyTextView == null && rootView != null)
			m_EmptyTextView = (TextView)rootView.findViewById(R.id.EmptyTextView);
		return m_EmptyTextView; 
	}

	protected void InitializeLayout(){}
}
