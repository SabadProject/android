package farayan.sabad.core.OnePlace.StoreCategory;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import farayan.sabad.core.OnePlace.Category.CategoryEntity;
import farayan.sabad.core.OnePlace.Store.StoreEntity;
import farayan.sabad.core.base.SabadEntityBase;

@DatabaseTable(tableName = StoreCategorySchema.StoreCategories)
public class StoreCategoryEntity extends SabadEntityBase<StoreCategoryEntity>
{
	@DatabaseField(columnName = StoreCategorySchema.Store, foreign = true)
	public StoreEntity Store;

	@DatabaseField(columnName = StoreCategorySchema.Category, foreign = true)
	public CategoryEntity Category;

	public StoreCategoryEntity() {

	}

	public StoreCategoryEntity(StoreEntity store, CategoryEntity category) {
		this.Store = store;
		this.Category = category;
	}

	@Override
	public boolean NeedsRefresh() {
		return Store == null;
	}
}
