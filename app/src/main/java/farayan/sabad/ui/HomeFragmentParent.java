package farayan.sabad.ui;
import android.os.Bundle;
import android.view.*;
import android.util.*;

import farayan.commons.FarayanUtility;
import farayan.sabad.R;
import farayan.sabad.ui.Core.*;
public abstract class HomeFragmentParent extends SabadBaseFragment {
	private View rootView = null;

	protected static final String tag = "HomeFragmentParent";


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		NullLoadedViews();
		long start = System.currentTimeMillis();
		rootView = inflater.inflate(R.layout.fragment_home, container, false);
		long finish = System.currentTimeMillis();
		Log.i("Performance", String.format("inflating fragment_home takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));
		FarayanUtility.OverrideFonts(rootView,getDefaultFont());
		InitializeLayout();
		return rootView;
	}

	private void NullLoadedViews() {
		m_GroupsRecyclerView = null;
		m_PurchaseSummary = null;
		m_QueryLayout = null;
		m_QueryEditText = null;
		m_RegisterButton = null;
		m_EditButton = null;
	}

	private androidx.recyclerview.widget.RecyclerView m_GroupsRecyclerView = null;

	protected androidx.recyclerview.widget.RecyclerView GroupsRecyclerView(){
		if(m_GroupsRecyclerView == null && rootView != null)
			m_GroupsRecyclerView = (androidx.recyclerview.widget.RecyclerView)rootView.findViewById(R.id.GroupsRecyclerView);
		return m_GroupsRecyclerView; 
	}

	private farayan.sabad.ui.PurchaseSummaryComponent m_PurchaseSummary = null;

	protected farayan.sabad.ui.PurchaseSummaryComponent PurchaseSummary(){
		if(m_PurchaseSummary == null && rootView != null)
			m_PurchaseSummary = (farayan.sabad.ui.PurchaseSummaryComponent)rootView.findViewById(R.id.PurchaseSummary);
		return m_PurchaseSummary; 
	}

	private farayan.commons.UI.CommonTextInputLayout m_QueryLayout = null;

	protected farayan.commons.UI.CommonTextInputLayout QueryLayout(){
		if(m_QueryLayout == null && rootView != null)
			m_QueryLayout = (farayan.commons.UI.CommonTextInputLayout)rootView.findViewById(R.id.QueryLayout);
		return m_QueryLayout; 
	}

	private farayan.commons.UI.CommonTextInputEditText m_QueryEditText = null;

	protected farayan.commons.UI.CommonTextInputEditText QueryEditText(){
		if(m_QueryEditText == null && rootView != null)
			m_QueryEditText = (farayan.commons.UI.CommonTextInputEditText)rootView.findViewById(R.id.QueryEditText);
		return m_QueryEditText; 
	}

	private com.google.android.material.button.MaterialButton m_RegisterButton = null;

	protected com.google.android.material.button.MaterialButton RegisterButton(){
		if(m_RegisterButton == null && rootView != null)
			m_RegisterButton = (com.google.android.material.button.MaterialButton)rootView.findViewById(R.id.RegisterButton);
		return m_RegisterButton; 
	}

	private com.google.android.material.button.MaterialButton m_EditButton = null;

	protected com.google.android.material.button.MaterialButton EditButton(){
		if(m_EditButton == null && rootView != null)
			m_EditButton = (com.google.android.material.button.MaterialButton)rootView.findViewById(R.id.EditButton);
		return m_EditButton; 
	}

	protected void InitializeLayout(){}
}
