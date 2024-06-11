package farayan.sabad.models.Category;

import android.content.Context;

import java.util.Collection;

import farayan.commons.UI.Core.FilterableEntityAdapter;
import farayan.commons.UI.Core.IEntityView;
import farayan.sabad.ui.CategoryBoxItemComponent;
import farayan.sabad.core.OnePlace.Category.CategoryEntity;

public class CategoryBoxAdapter extends FilterableEntityAdapter<CategoryEntity>
{
	public CategoryBoxAdapter(Context ctx, Collection<? extends CategoryEntity> items) {
		super(ctx, items);
	}

	@Override
	protected IEntityView<CategoryEntity> NewView(Context ctx) {
		return new CategoryBoxItemComponent(ctx);
	}
}
