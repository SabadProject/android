package farayan.commons;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public abstract class FarayanBaseFragment extends Fragment
{
	protected FarayanBaseFragmentActivity<? extends FarayanBaseFragment> HostActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(OptionsMenuAllowed());


		// TODO: 30/12/2017 uncomment for google analytics
	/*Tracker tracker = FarayanBaseApp.Instance().GoogleAnalyticsTracker();
        if (tracker != null) {
            String screenName = this.getClass().getName();
            tracker.setScreenName(screenName);
            tracker.send(new HitBuilders.AppViewBuilder().build());
        }*/
	}

	protected boolean OptionsMenuAllowed() {
		return true;
	}

	/*@Override
	public void onAttach(Activity activity) {
		if (!(activity instanceof FarayanBaseFragmentActivity))
			throw new RuntimeException("Activity is not FarayanBaseFragmentActivity");
		this.HostActivity = (FarayanBaseFragmentActivity<? extends FarayanBaseFragment>) activity;
		super.onAttach(activity);
	}*/

	@Override
	public void onAttach(@NonNull Context context) {
		if (!(context instanceof FarayanBaseFragmentActivity))
			throw new RuntimeException("Activity is not FarayanBaseFragmentActivity");
		this.HostActivity = (FarayanBaseFragmentActivity<? extends FarayanBaseFragment>) context;
		super.onAttach(context);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		UpgradeUtility.FragmentResumed((AppCompatActivity) getActivity(), FarayanBaseApp.Instance().Expiration());
		super.onResume();
	}

	public int TitleID() {
		return 0;
	}

	public boolean Backable() {
		return true;
	}

	public boolean HandleBackPressed() {
		return false;
	}

	public String DynamicTitle() {
		return null;
	}
}
