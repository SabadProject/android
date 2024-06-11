package farayan.sabad.ui.Core;

import android.content.Context;
import android.graphics.Typeface;

import farayan.commons.FarayanBaseFragmentActivity;
import farayan.commons.IFragmentEvents;
import farayan.sabad.R;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class SabadBaseActivity extends FarayanBaseFragmentActivity<SabadBaseFragment>
{
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
	}

	protected Typeface getDefaultFont() {
		return null;
	}

	@Override
	protected int FragmentPlaceID() {
		return R.id.FragmentPlace;
	}

	@Override
	public boolean OnFragmentCalled(SabadBaseFragment caller, IFragmentEvents event, Object value) {
		return false;
	}
}
