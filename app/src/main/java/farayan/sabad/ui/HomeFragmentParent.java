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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("flow", "HomeFragmentParent: onCreateView");
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
    }

    protected androidx.compose.ui.platform.ComposeView GroupsRecyclerView() {
        if (m_GroupsRecyclerView == null && rootView != null)
            m_GroupsRecyclerView = rootView.findViewById(R.id.CategoriesComposeView);
        return m_GroupsRecyclerView;
    }

    protected void InitializeLayout() {
    }
}
