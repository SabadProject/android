package farayan.sabad.models.InvoiceItem;

import android.content.Context;
import android.view.ViewGroup;

import java.util.Collection;

import farayan.commons.UI.Core.EntityRecyclerViewAdapter;
import farayan.commons.UI.Core.IEntityView;
import farayan.sabad.core.OnePlace.InvoiceItem.InvoiceItemEntity;
import farayan.sabad.ui.InvoiceItemsItemComponent;

public class InvoiceItemRecyclerAdapter extends EntityRecyclerViewAdapter<InvoiceItemEntity>
{
	public InvoiceItemRecyclerAdapter(Context ctx, Collection<? extends InvoiceItemEntity> items) {
		super(ctx, items);
	}

	@Override
	protected IEntityView<InvoiceItemEntity> NewView(ViewGroup parent, int viewGroup) {
		return new InvoiceItemsItemComponent(Ctx);
	}
}
