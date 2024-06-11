package farayan.sabad.core.OnePlace.CategoryGroup;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import farayan.sabad.core.OnePlace.Category.CategoryEntity;
import farayan.sabad.core.OnePlace.Group.GroupEntity;
import farayan.sabad.core.base.SabadEntityBase;

@DatabaseTable(tableName = CategoryGroupSchema.CategoryGroups)
public class CategoryGroupEntity extends SabadEntityBase<CategoryGroupEntity>
{
	@DatabaseField(columnName = CategoryGroupSchema.Group, foreign = true)
	public GroupEntity Group;

	@DatabaseField(columnName = CategoryGroupSchema.Category, foreign = true)
	public CategoryEntity Category;

	public CategoryGroupEntity() {
	}

	public CategoryGroupEntity(GroupEntity Group, CategoryEntity category) {
		Group = Group;
		Category = category;
	}

	@Override
	public boolean NeedsRefresh() {
		return Group == null;
	}
}
