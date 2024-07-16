package farayan.sabad.ui;

import android.content.Context;
import android.util.AttributeSet;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import dagger.hilt.android.AndroidEntryPoint;
import farayan.commons.Commons.Rial;
import farayan.commons.FarayanUtility;
import farayan.commons.UI.Core.IEntityView;
import farayan.sabad.core.OnePlace.Group.IGroupRepo;
import farayan.sabad.core.OnePlace.InvoiceItem.InvoiceItemEntity;
import farayan.sabad.core.OnePlace.Unit.IUnitRepo;
import farayan.sabad.core.model.product.IProductRepo;

@AndroidEntryPoint
public class InvoiceItemsItemComponent extends InvoiceItemsItemComponentParent implements IEntityView<InvoiceItemEntity>
{
	@Inject
	protected IGroupRepo TheGroupRepo;

	@Inject
	protected IProductRepo TheProductRepo;

	@Inject
	protected IUnitRepo TheUnitRepo;

	private InvoiceItemEntity TheInvoiceItem;

	public InvoiceItemsItemComponent(Context context) {
		super(context);
	}

	public InvoiceItemsItemComponent(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InvoiceItemsItemComponent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public InvoiceItemsItemComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public void DisplayEntity(InvoiceItemEntity entity) {
		TheInvoiceItem = entity;
		TitleTextView().setText(String.format(
				"%s %s",
				entity.Group.Refreshed(TheGroupRepo).DisplayableName,
				FarayanUtility.Or(entity.Product.Refreshed(TheProductRepo).DisplayableName,"")
		));
		if (entity.Quantity == 1) {
			DetailsTextView().setText(new Rial(entity.Total).Textual(getResources()));
		} else {
			DetailsTextView().setText(String.format(
					"%s %s، فی %s، جمعا: %s",
					FarayanUtility.MoneyFormatted(entity.Quantity, true, false),
					FarayanUtility.CatchException(() -> FarayanUtility.Or(entity.Unit.Refreshed(TheUnitRepo).DisplayableName, ""), x -> ""),
					new Rial(entity.Fee).Textual(getResources()),
					new Rial(entity.Total).Textual(getResources())
			));
		}
	}
}
