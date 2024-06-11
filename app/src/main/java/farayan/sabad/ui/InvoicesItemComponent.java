package farayan.sabad.ui;

import android.content.Context;
import android.util.AttributeSet;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import dagger.hilt.android.AndroidEntryPoint;
import farayan.commons.Commons.Rial;
import farayan.commons.FarayanUtility;
import farayan.commons.PersianCalendar;
import farayan.commons.UI.Core.IEntityView;
import farayan.commons.UI.Core.IGenericEvent;
import farayan.sabad.core.OnePlace.Invoice.InvoiceEntity;
import farayan.sabad.R;
import farayan.sabad.core.OnePlace.Store.IStoreRepo;

@AndroidEntryPoint
public class InvoicesItemComponent extends InvoicesItemComponentParent implements IEntityView<InvoiceEntity>
{
	private InvoiceEntity TheInvoice;

	@Inject
	public IStoreRepo TheStoreRepo;

	private final IGenericEvent<InvoiceEntity> OnClicked;
	private final IGenericEvent<InvoiceEntity> OnRemoved;

	public InvoicesItemComponent(Context context, IGenericEvent<InvoiceEntity> onClicked, IGenericEvent<InvoiceEntity> onRemoved) {
		super(context);
		OnClicked = onClicked;
		OnRemoved = onRemoved;
	}

	public InvoicesItemComponent(Context context, AttributeSet attrs) {
		super(context, attrs);
		OnClicked = null;
		OnRemoved = null;
	}

	public InvoicesItemComponent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		OnClicked = null;
		OnRemoved = null;
	}

	public InvoicesItemComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		OnClicked = null;
		OnRemoved = null;
	}

	@Override
	protected void InitializeComponents() {
		Container().setOnClickListener(view -> IGenericEvent.Exec(OnClicked, TheInvoice));
		DeleteImageButton().setOnClickListener(view -> FarayanUtility.ShowToastFormatted(getContext(), "برای حذف، انگشت‌تان را چند لحظه‌ی برروی این آیکن نگه دارید"));
		DeleteImageButton().setOnLongClickListener(view -> {
			IGenericEvent.Exec(OnRemoved, TheInvoice);
			return true;
		});
		super.InitializeComponents();
	}

	@Override
	public void DisplayEntity(InvoiceEntity entity) {
		TheInvoice = entity;
		String instant = new PersianCalendar(entity.Instant).getPersianDateSentence(true);
		String line1 = entity.Seller == null
				? getResources().getString(R.string.InvoiceLine1WithoutSeller, instant)
				: getResources().getString(R.string.InvoiceLine1WithSeller, instant, entity.Seller.Refreshed(TheStoreRepo).DisplayableName);
		Line1TextView().setText(line1);
		Line2TextView().setText(getResources().getString(
				R.string.InvoiceLine2,
				FarayanUtility.MoneyFormatted(entity.ItemsCountSum, true, false),
				FarayanUtility.MoneyFormatted(entity.ItemsQuantitySum, true, false),
				new Rial(entity.ItemsPriceSum).Textual(getResources())
		));
		DeleteImageButton().setVisibility(OnRemoved == null ? GONE : VISIBLE);
	}
}
