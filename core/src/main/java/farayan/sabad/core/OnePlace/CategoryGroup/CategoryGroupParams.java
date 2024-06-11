package farayan.sabad.core.OnePlace.CategoryGroup;

import org.apache.commons.lang3.ArrayUtils;

import farayan.commons.QueryBuilderCore.EntityFilter;
import farayan.commons.QueryBuilderCore.PropertyFilter;
import farayan.sabad.core.OnePlace.Category.CategoryEntity;
import farayan.sabad.core.OnePlace.Group.GroupEntity;
import farayan.sabad.core.base.SabadParamsBase;

public class CategoryGroupParams extends SabadParamsBase<CategoryGroupEntity>
{
	public EntityFilter<GroupEntity> Group;
	public EntityFilter<CategoryEntity> Category;

	@Override
	public PropertyFilter[] Filters() {
		return ArrayUtils.addAll(
				super.Filters(),
				new PropertyFilter(CategoryGroupSchema.Group, () -> Group),
				new PropertyFilter(CategoryGroupSchema.Category, () -> Category)
		);
	}
}
