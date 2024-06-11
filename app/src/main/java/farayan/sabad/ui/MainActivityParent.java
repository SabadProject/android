package farayan.sabad.ui;
import android.os.Bundle;
import android.widget.*;
import android.util.*;

import farayan.commons.FarayanUtility;
import farayan.sabad.R;
import farayan.sabad.ui.Core.*;
public abstract class MainActivityParent extends SabadBaseActivity {
	protected static final String tag = "MainActivityParent";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NullLoadedViews();
		long start = System.currentTimeMillis();
		setContentView(R.layout.activity_main);
		long finish = System.currentTimeMillis();
		Log.i("Performance", String.format("inflating activity_main takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));
		FarayanUtility.OverrideFonts(findViewById(android.R.id.content) ,getDefaultFont());
		InitializeLayout();	}

	private void NullLoadedViews() {
		m_FragmentPlace = null;
	}

	private LinearLayout m_FragmentPlace;
	protected LinearLayout FragmentPlace(){
		if(m_FragmentPlace == null)
			m_FragmentPlace = (LinearLayout)findViewById(R.id.FragmentPlace);
		return m_FragmentPlace; 
	}

	protected void InitializeLayout(){}
}
