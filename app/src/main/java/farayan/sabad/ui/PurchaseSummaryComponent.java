package farayan.sabad.ui;

import android.content.Context;
import android.util.AttributeSet;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import dagger.hilt.android.AndroidEntryPoint;
import farayan.commons.Commons.Rial;
import farayan.commons.FarayanUtility;
import farayan.sabad.core.OnePlace.Group.IGroupRepo;
import farayan.sabad.core.OnePlace.InvoiceItem.IInvoiceItemRepo;
import farayan.sabad.R;
import farayan.sabad.SabadConstants;
import farayan.sabad.SabadUtility;

@AndroidEntryPoint
public class PurchaseSummaryComponent extends PurchaseSummaryComponentParent
{
	@Inject
	public void Inject(
			IGroupRepo groupRepo,
			IInvoiceItemRepo invoiceItemRepo
	) {
		TheGroupRepo = groupRepo;
		TheInvoiceItemRepo = invoiceItemRepo;
		ReloadSummary(SabadConstants.TheCoefficient);
	}

	private IGroupRepo TheGroupRepo;
	private IInvoiceItemRepo TheInvoiceItemRepo;

	public PurchaseSummaryComponent(Context context) {
		super(context);
	}

	public PurchaseSummaryComponent(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PurchaseSummaryComponent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PurchaseSummaryComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void InitializeComponents() {
		ContainerLayout().setOnClickListener(view -> ReloadSummary(SabadConstants.TheCoefficient));
	}

	public void ReloadSummary(Rial.Coefficients coefficient) {

		SabadUtility.PurchaseSummary purchaseSummary = SabadUtility.PurchaseSummary(TheGroupRepo, TheInvoiceItemRepo);

		PickedItemsTextView().setText(getContext().getString(
				R.string.PurchaseSummaryPickedItems,
				FarayanUtility.MoneyFormatted(purchaseSummary.ItemedCount, true, false),
				FarayanUtility.MoneyFormatted(purchaseSummary.QuantitiesSum, true, false)
		));

		RemainedItemsTextView().setText(getContext().getString(
				R.string.PurchaseSummaryRemainedItems,
				FarayanUtility.MoneyFormatted(purchaseSummary.RemainedCount, true, false)
		));

		SubtotalTextView().setText(getContext().getString(
				R.string.SubtotalTextView,
				new Rial(coefficient, purchaseSummary.PricesSum).Textual(getResources())
		));
	}
}
