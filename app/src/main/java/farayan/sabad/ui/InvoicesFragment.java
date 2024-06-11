package farayan.sabad.ui;

import android.view.View;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import farayan.commons.Commons.RecyclerViewOrientations;
import farayan.commons.FarayanUtility;
import farayan.commons.QueryBuilderCore.ComparableFilter;
import farayan.commons.QueryBuilderCore.SortConfig;
import farayan.commons.QueryBuilderCore.SortDirections;
import farayan.sabad.constants.SabadFragmentEvents;
import farayan.sabad.core.OnePlace.Invoice.IInvoiceRepo;
import farayan.sabad.models.Invoice.InvoiceAdapter;
import farayan.sabad.core.OnePlace.Invoice.InvoiceEntity;
import farayan.sabad.core.OnePlace.Invoice.InvoiceParams;
import farayan.sabad.R;
import farayan.sabad.core.OnePlace.Invoice.InvoiceSchema;

@AndroidEntryPoint
public class InvoicesFragment extends InvoicesFragmentParent
{
	private IInvoiceRepo TheInvoiceRepo;
	private boolean reloaded;

	@Override
	public void onResume() {
		EnsureReload();
		super.onResume();
	}

	@Override
	public void onPause() {
		reloaded = false;
		super.onPause();
	}

	@Inject
	public void Inject(IInvoiceRepo invoiceRepo) {
		TheInvoiceRepo = invoiceRepo;
		EnsureReload();
	}

	private void EnsureReload() {
		if (reloaded)
			return;
		if (InvoicesRecyclerView() == null)
			return;
		if (TheInvoiceRepo == null)
			return;
		Reload();
		reloaded = true;
	}

	private void Reload() {
		InvoiceParams params = new InvoiceParams();
		params.Deleted = new ComparableFilter<>(false);
		params.Sorts.add(new SortConfig(InvoiceSchema.Instant, SortDirections.Descending));
		List<InvoiceEntity> invoices = TheInvoiceRepo.All(params);
		if (FarayanUtility.IsUsable(invoices)) {
			InvoicesRecyclerView().setVisibility(View.VISIBLE);
			EmptyTextView().setVisibility(View.GONE);
			InvoiceAdapter invoiceAdapter = new InvoiceAdapter(
					getActivity(),
					invoices,
					invoiceEntity -> {
						if (invoiceEntity.ItemsQuantitySum == 0) {
							FarayanUtility.ShowToastFormatted(getActivity(), "صورتحساب خالی است");
							return;
						}
						HostActivity.OnFragmentCalled(null, SabadFragmentEvents.DisplayInvoice, invoiceEntity);
					},
					invoiceEntity -> {
						TheInvoiceRepo.Hide(invoiceEntity);
						FarayanUtility.ShowToastFormatted(getActivity(), "صورتحساب حذف شد");
						Reload();
					}
			);
			InvoicesRecyclerView().setAdapter(invoiceAdapter);
		} else {
			InvoicesRecyclerView().setVisibility(View.GONE);
			EmptyTextView().setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void InitializeLayout() {
		FarayanUtility.prepareRecyclerView(
				InvoicesRecyclerView(),
				RecyclerViewOrientations.Vertical,
				null,
				true
		);
		EnsureReload();
	}

	@Override
	public int TitleID() {
		return R.string.InvoicesFragmentTitle;
	}
}
