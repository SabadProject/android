package farayan.sabad.models.Product;

import android.content.Context;

import java.util.Collection;

import farayan.commons.UI.Core.FilterableEntityAdapter;
import farayan.commons.UI.Core.IEntityView;
import farayan.sabad.core.model.product.ProductEntity;
import farayan.sabad.ui.ProductBoxItemComponent;

public class ProductBoxAdapter extends FilterableEntityAdapter<ProductEntity> {
    public ProductBoxAdapter(Context ctx, Collection<? extends ProductEntity> items) {
        super(ctx, items);
    }

    @Override
    protected IEntityView<ProductEntity> NewView(Context ctx) {
        return new ProductBoxItemComponent(ctx);
    }
}
