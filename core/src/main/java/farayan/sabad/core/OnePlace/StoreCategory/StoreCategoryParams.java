package farayan.sabad.core.OnePlace.StoreCategory;

import org.apache.commons.lang3.ArrayUtils;

import farayan.commons.QueryBuilderCore.EntityFilter;
import farayan.commons.QueryBuilderCore.PropertyFilter;
import farayan.sabad.core.OnePlace.Category.CategoryEntity;
import farayan.sabad.core.OnePlace.Store.StoreEntity;
import farayan.sabad.core.base.SabadParamsBase;

public class StoreCategoryParams extends SabadParamsBase<StoreCategoryEntity>
{
	public EntityFilter<StoreEntity> Store;

	public EntityFilter<CategoryEntity> Category;

	@Override
	public PropertyFilter[] Filters() {
		return ArrayUtils.addAll(
				super.Filters(),
				new PropertyFilter(StoreCategorySchema.Store, () -> Store),
				new PropertyFilter(StoreCategorySchema.Category, () -> Category)
		);
	}
}
