package farayan.commons;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public abstract class FarayanBaseFragmentActivity<TFragment extends FarayanBaseFragment> extends FarayanBaseCoreActivity
{
	private final FragmentManager.OnBackStackChangedListener OnBackStackChangedListener = this::FragmentChanged;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getFragmentManager().addOnBackStackChangedListener(OnBackStackChangedListener);
	}

	@Override
	public void onBackPressed() {
		if (getVisibleFragment() != null) {
			boolean backHandled = getVisibleFragment().HandleBackPressed();
			if (backHandled)
				return;
		}

		int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
		if (backStackEntryCount > 1) {
			getSupportFragmentManager().popBackStackImmediate();

			String fragmentTitle = fragmentTitle(getVisibleFragment());
			ActionBar actionBar = getSupportActionBar();
			if (actionBar != null && FarayanUtility.IsUsable(fragmentTitle)) {
				actionBar.setTitle(fragmentTitle);
			}
		} else {
			finish();
		}
	}

	final List<TFragment> fragments = new ArrayList<>();

	public void startFragment(TFragment fragment) {
		if (getVisibleFragment() != null && getVisibleFragment().getClass().getName().equalsIgnoreCase(fragment.getClass().getName()))
			return;
		String fragmentTitle = fragmentTitle(fragment);
		if (!fragments.contains(fragment))
			fragments.add(fragment);
		androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.setBreadCrumbTitle(fragmentTitle);
		fragmentTransaction.setBreadCrumbShortTitle(fragmentTitle);
		fragmentTransaction.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
		fragmentTransaction.replace(FragmentPlaceID(), fragment);
		if (fragment.Backable())
			fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null && FarayanUtility.IsUsable(fragmentTitle)) {
			actionBar.setTitle(fragmentTitle);
		}
	}

	private String fragmentTitle(TFragment fragment) {
		String title = fragment.DynamicTitle();
		if (FarayanUtility.IsUsable(title))
			return title;
		if (fragment.TitleID() != 0)
			return getString(fragment.TitleID());

		return "";
	}

	protected void FragmentChanged() {
		InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		keyboard.hideSoftInputFromWindow(findViewById(android.R.id.content).getWindowToken(), 0);
	}

	protected abstract int FragmentPlaceID();

	protected boolean startFragment(Class<? extends TFragment> fragmentClass) {

		TFragment fragment = null;
		String className = fragmentClass.getName();
		for (int i = 0; i < fragments.size(); i++) {
			if (fragments.get(i).getClass().getName().equals(className)) {
				fragment = fragments.get(i);
				break;
			}
		}
		if (fragment == null)
			return false;
		startFragment(fragment);
		return true;
	}

	protected <T> T getFragment(Class<? extends T> fragmentClass) {

		T fragment = null;
		String className = fragmentClass.getName();
		for (int i = 0; i < fragments.size(); i++) {
			if (fragments.get(i).getClass().getName().equals(className))
				return (T) fragments.get(i);
		}
		return null;
	}

	public boolean RemoveFragment(Class<? extends TFragment> fragmentClass) {
		String className = fragmentClass.getName();
		for (int index = 0; index < fragments.size(); index++) {
			if (fragments.get(index).getClass().getName().equals(className)) {
				fragments.remove(index);
				return true;
			}
		}
		return false;
	}

	public TFragment getVisibleFragment() {
		Fragment visibleFragment = getSupportFragmentManager().findFragmentById(FragmentPlaceID());
		if (visibleFragment == null)
			return null;
		try {
			TFragment tFragment = (TFragment) visibleFragment;
			return tFragment;
		} catch (ClassCastException exception) {
			return null;
		}
	}

	public boolean OnFragmentCalled(TFragment caller, IFragmentEvents event) {
		return OnFragmentCalled(caller, event, null);
	}

	public abstract boolean OnFragmentCalled(TFragment caller, IFragmentEvents event, Object value);

}
