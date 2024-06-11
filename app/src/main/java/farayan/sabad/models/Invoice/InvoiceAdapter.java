package farayan.sabad.models.Invoice;

import android.content.Context;
import android.view.ViewGroup;

import java.util.Collection;

import farayan.commons.UI.Core.EntityRecyclerViewAdapter;
import farayan.commons.UI.Core.IEntityView;
import farayan.commons.UI.Core.IGenericEvent;
import farayan.sabad.ui.InvoicesItemComponent;
import farayan.sabad.core.OnePlace.Invoice.InvoiceEntity;

public class InvoiceAdapter extends EntityRecyclerViewAdapter<InvoiceEntity>
{
	private final IGenericEvent<InvoiceEntity> onClicked;
	private final IGenericEvent<InvoiceEntity> onRemoved;

	public InvoiceAdapter(Context ctx, Collection<? extends InvoiceEntity> items, IGenericEvent<InvoiceEntity> onClicked, IGenericEvent<InvoiceEntity> onRemoved) {
		super(ctx, items);
		this.onClicked = onClicked;
		this.onRemoved = onRemoved;
	}

	@Override
	protected IEntityView<InvoiceEntity> NewView(ViewGroup parent, int viewGroup) {
		return new InvoicesItemComponent(Ctx, onClicked, onRemoved);
	}
}
