package farayan.sabad.core.OnePlace.Category;

import org.apache.commons.lang3.ArrayUtils;

import farayan.commons.QueryBuilderCore.PropertyFilter;
import farayan.commons.QueryBuilderCore.TextFilter;
import farayan.sabad.core.base.SabadParamsBase;

public class CategoryParams extends SabadParamsBase<CategoryEntity>
{
	public TextFilter DisplayableName;

	public TextFilter QueryableName;

	@Override
	public PropertyFilter[] Filters() {
		return ArrayUtils.addAll(
				super.Filters(),
				new PropertyFilter(CategorySchema.DisplayableName, () -> DisplayableName),
				new PropertyFilter(CategorySchema.QueryableName, () -> QueryableName)
		);
	}
}
