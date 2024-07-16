package farayan.sabad.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
import farayan.commons.IFragmentEvents;
import farayan.sabad.R;
import farayan.sabad.constants.SabadFragmentEvents;
import farayan.sabad.core.OnePlace.Invoice.InvoiceEntity;
import farayan.sabad.ui.Core.SabadBaseFragment;

@AndroidEntryPoint
public class MainActivity extends MainActivityParent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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
        if (Objects.requireNonNull(event) == SabadFragmentEvents.DisplayInvoice) {
            RemoveFragment(InvoiceFragment.class);
            InvoiceFragment invoiceFragment = new InvoiceFragment();
            startFragment(invoiceFragment);
            invoiceFragment.DisplayEntity((InvoiceEntity) value);
        }
        return true;
    }

}
