package farayan.sabad.models.Product;

import android.content.Context;

import java.util.Collection;

import farayan.commons.UI.Core.IEntityView;
import farayan.commons.UI.Core.FilterableEntityAdapter;
import farayan.sabad.ui.ProductBoxItemComponent;
import farayan.sabad.core.OnePlace.Product.ProductEntity;

public class ProductBoxAdapter extends FilterableEntityAdapter<ProductEntity>
{
	public ProductBoxAdapter(Context ctx, Collection<? extends ProductEntity> items) {
		super(ctx, items);
	}

	@Override
	protected IEntityView<ProductEntity> NewView(Context ctx) {
		return new ProductBoxItemComponent(ctx);
	}
}
