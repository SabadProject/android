package farayan.sabad.ui;

import androidx.annotation.NonNull;
import dagger.hilt.android.AndroidEntryPoint;
import farayan.commons.IFragmentEvents;
import farayan.sabad.constants.SabadFragmentEvents;
import farayan.sabad.core.OnePlace.Invoice.InvoiceEntity;
import farayan.sabad.R;
import farayan.sabad.ui.Core.SabadBaseFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

@AndroidEntryPoint
public class MainActivity extends MainActivityParent
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!startFragment(HomeFragment.class))
			startFragment(new HomeFragment());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if (item.getItemId() == R.id.DisplayInvoicesMenu) {
			RemoveFragment(InvoicesFragment.class);
			startFragment(new InvoicesFragment());
			return true;
		}
		if (item.getItemId() == R.id.AboutMenu) {
			Intent aboutIntent = new Intent(this, AboutActivity.class);
			startActivity(aboutIntent);
			return true;
		}
		return false;
	}

	@Override
	public boolean OnFragmentCalled(SabadBaseFragment caller, IFragmentEvents iEvent, Object value) {
		SabadFragmentEvents event = (SabadFragmentEvents) iEvent;
		switch (event) {
			case DisplayInvoice:
				RemoveFragment(InvoiceFragment.class);
				InvoiceFragment invoiceFragment = new InvoiceFragment();
				startFragment(invoiceFragment);
				invoiceFragment.DisplayEntity((InvoiceEntity) value);
				break;
		}
		return true;
	}

	@Override
	protected int FragmentPlaceID() {
		return R.id.FragmentPlace;
	}
}
