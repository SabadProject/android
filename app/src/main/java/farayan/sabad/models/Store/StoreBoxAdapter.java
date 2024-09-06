package farayan.sabad.models.Store;

import android.content.Context;

import java.util.Collection;

import farayan.commons.UI.Core.FilterableEntityAdapter;
import farayan.commons.UI.Core.IEntityView;
import farayan.sabad.core.OnePlace.Store.StoreEntity;
import farayan.sabad.ui.StoreBoxItemComponent;

public class StoreBoxAdapter extends FilterableEntityAdapter<StoreEntity>
{
	public StoreBoxAdapter(Context ctx, Collection<? extends StoreEntity> items) {
		super(ctx, items);
	}

	@Override
	protected IEntityView<StoreEntity> NewView(Context ctx) {
		return new StoreBoxItemComponent(ctx);
	}
}
