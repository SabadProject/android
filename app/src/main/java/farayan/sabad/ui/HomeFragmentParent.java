package farayan.sabad.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import farayan.commons.FarayanUtility;
import farayan.sabad.R;
import farayan.sabad.ui.Core.SabadBaseFragment;

public abstract class HomeFragmentParent extends SabadBaseFragment {
    protected static final String tag = "HomeFragmentParent";
    private View rootView = null;
    private androidx.compose.ui.platform.ComposeView m_GroupsRecyclerView = null;
    private farayan.sabad.ui.PurchaseSummaryComponent m_PurchaseSummary = null;
    private farayan.commons.UI.CommonTextInputLayout m_QueryLayout = null;
    private farayan.commons.UI.CommonTextInputEditText m_QueryEditText = null;
    private com.google.android.material.button.MaterialButton m_RegisterButton = null;
    private com.google.android.material.button.MaterialButton m_EditButton = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NullLoadedViews();
        long start = System.currentTimeMillis();
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        long finish = System.currentTimeMillis();
        Log.i("Performance", String.format("inflating fragment_home takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));
        FarayanUtility.OverrideFonts(rootView, getDefaultFont());
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

    protected androidx.compose.ui.platform.ComposeView GroupsRecyclerView() {
        if (m_GroupsRecyclerView == null && rootView != null)
            m_GroupsRecyclerView = rootView.findViewById(R.id.CategoriesComposeView);
        return m_GroupsRecyclerView;
    }

    protected farayan.sabad.ui.PurchaseSummaryComponent PurchaseSummary() {
        if (m_PurchaseSummary == null && rootView != null)
            m_PurchaseSummary = rootView.findViewById(R.id.PurchaseSummary);
        return m_PurchaseSummary;
    }

    protected farayan.commons.UI.CommonTextInputLayout QueryLayout() {
        if (m_QueryLayout == null && rootView != null)
            m_QueryLayout = rootView.findViewById(R.id.QueryLayout);
        return m_QueryLayout;
    }

    protected farayan.commons.UI.CommonTextInputEditText QueryEditText() {
        if (m_QueryEditText == null && rootView != null)
            m_QueryEditText = rootView.findViewById(R.id.QueryEditText);
        return m_QueryEditText;
    }

    protected com.google.android.material.button.MaterialButton RegisterButton() {
        if (m_RegisterButton == null && rootView != null)
            m_RegisterButton = rootView.findViewById(R.id.RegisterButton);
        return m_RegisterButton;
    }

    protected com.google.android.material.button.MaterialButton EditButton() {
        if (m_EditButton == null && rootView != null)
            m_EditButton = rootView.findViewById(R.id.EditButton);
        return m_EditButton;
    }

    protected void InitializeLayout() {
    }
}
