package farayan.sabad.ui;

import static farayan.sabad.FaraBankConstantsKt.FaraBankPackageID;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import farayan.commons.Commons.RecyclerViewOrientations;
import farayan.commons.FarayanUtility;
import farayan.commons.PersianCalendar;
import farayan.commons.QueryBuilderCore.EntityFilter;
import farayan.commons.QueryBuilderCore.GsonUtility;
import farayan.commons.UI.Core.IEntityView;
import farayan.farabank.core.FaraBankGlobalsKt;
import farayan.sabad.R;
import farayan.sabad.SabadConstants;
import farayan.sabad.core.FaraBank.FaraBankInvoice;
import farayan.sabad.core.OnePlace.Group.IGroupRepo;
import farayan.sabad.core.OnePlace.Invoice.IInvoiceRepo;
import farayan.sabad.core.OnePlace.Invoice.InvoiceEntity;
import farayan.sabad.core.OnePlace.Invoice.InvoicePortable;
import farayan.sabad.core.OnePlace.InvoiceItem.IInvoiceItemRepo;
import farayan.sabad.core.OnePlace.InvoiceItem.InvoiceItemEntity;
import farayan.sabad.core.OnePlace.InvoiceItem.InvoiceItemParams;
import farayan.sabad.core.OnePlace.Store.IStoreRepo;
import farayan.sabad.core.OnePlace.Store.StorePortable;
import farayan.sabad.core.OnePlace.Unit.IUnitRepo;
import farayan.sabad.core.OnePlace.Unit.UnitPortable;
import farayan.sabad.core.model.product.IProductRepo;
import farayan.sabad.core.model.product.ProductPortable;
import farayan.sabad.models.InvoiceItem.InvoiceItemRecyclerAdapter;

@AndroidEntryPoint
public class InvoiceFragment extends InvoiceFragmentParent implements IEntityView<InvoiceEntity> {
    private IInvoiceItemRepo TheInvoiceItemRepo;
    private IProductRepo TheProductRepo;
    private IStoreRepo TheStoreRepo;
    private IGroupRepo TheGroupRepo;
    private IUnitRepo TheUnitRepo;
    private IInvoiceRepo TheInvoiceRepo;
    private boolean reloaded;
    private InvoiceEntity TheInvoice;

    @Inject
    public void Inject(
            IInvoiceItemRepo invoiceItemRepo,
            IStoreRepo storeRepo,
            IProductRepo productRepo,
            IInvoiceRepo invoiceRepo,
            IGroupRepo groupRepo,
            IUnitRepo unitRepo
    ) {
        TheInvoiceItemRepo = invoiceItemRepo;
        TheStoreRepo = storeRepo;
        TheProductRepo = productRepo;
        TheInvoiceRepo = invoiceRepo;
        TheGroupRepo = groupRepo;
        TheUnitRepo = unitRepo;
    }

    @Override
    protected void InitializeLayout() {
        FarayanUtility.prepareRecyclerView(InvoiceItemsRecyclerView(), RecyclerViewOrientations.Vertical, null, true);
        EnsureReloaded();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.invoice_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.FaraBankMenu) {
            List<InvoiceItemEntity> items = TheInvoiceItemRepo.InvoiceParams(TheInvoice);
            InvoicePortable invoicePortable = new InvoicePortable(
                    TheInvoice,
                    items,
                    TheStoreRepo,
                    TheProductRepo,
                    TheInvoiceRepo,
                    TheGroupRepo,
                    TheUnitRepo
            );

            FaraBankInvoice faraBankInvoice = new FaraBankInvoice(
                    invoicePortable,
                    TheInvoice.Seller == null ? null : new StorePortable(TheInvoice.Seller.Refreshed(TheStoreRepo)),
                    items.stream()
                            .map(x -> x.Refreshed(TheInvoiceItemRepo).Product.Refreshed(TheProductRepo))
                            .peek(x -> x.Group.Refreshed(TheGroupRepo))
                            .distinct()
                            .map(ProductPortable::new)
                            .collect(Collectors.toList()),
                    items.stream()
                            .filter(x -> x.Refreshed(TheInvoiceItemRepo).Unit != null)
                            .map(x -> x.Unit.Refreshed(TheUnitRepo))
                            .distinct()
                            .map(UnitPortable::new)
                            .collect(Collectors.toList())
            );
            Gson gson = GsonUtility.prepared();
            String json = gson.toJson(faraBankInvoice);

            Intent intent = new Intent(FaraBankGlobalsKt.SabadAction);
            intent.setPackage(FaraBankPackageID);
            intent.putExtra(FaraBankGlobalsKt.SabadInvoiceKey, json);
            if (intent.resolveActivity(requireActivity().getPackageManager()) == null) {
                FarayanUtility.ShowToastFormatted(getActivity(), "فرابانک در گوشی شما نصب نیست");
                return true;
            }
            startActivityForResult(intent, SabadConstants.FaraBankExportInvoiceRequestCode);
            return true;
        }
        return false;
    }

    @Override
    public void DisplayEntity(InvoiceEntity entity) {
        TheInvoice = entity;
        EnsureReloaded();
    }

    private void EnsureReloaded() {
        if (reloaded)
            return;
        if (TheInvoice == null)
            return;
        if (SummaryInvoicesItem() == null)
            return;
        SummaryInvoicesItem().DisplayEntity(TheInvoice);
        InvoiceItemParams params = new InvoiceItemParams();
        params.Invoice = new EntityFilter<>(TheInvoice);
        List<InvoiceItemEntity> items = TheInvoiceItemRepo.All(params);
        InvoiceItemRecyclerAdapter adapter = new InvoiceItemRecyclerAdapter(getActivity(), items);
        InvoiceItemsRecyclerView().setAdapter(adapter);
        reloaded = true;
    }

    @Override
    public String DynamicTitle() {
        return invoiceSummary();
    }

    @Override
    public int TitleID() {
        return R.string.InvoiceFragmentTitle;
    }

    private String invoiceSummary() {
        if (TheInvoice == null)
            return null;
        String instant = new PersianCalendar(TheInvoice.Instant).getPersianDateTimeStandard(true);
        if (TheInvoice.Seller == null)
            return instant;
        return String.format("%s از %s", instant, TheInvoice.Seller.Refreshed(TheStoreRepo).DisplayableName);
    }
}
