package farayan.sabad.core.OnePlace.Store;

import org.apache.commons.lang3.ArrayUtils;

import farayan.commons.QueryBuilderCore.PropertyFilter;
import farayan.commons.QueryBuilderCore.TextFilter;
import farayan.sabad.core.base.SabadParamsBase;

public class StoreParams extends SabadParamsBase<StoreEntity>
{
	public TextFilter DisplayableName;

	public TextFilter QueryableName;

	@Override
	public PropertyFilter[] Filters() {
		return ArrayUtils.addAll(
				super.Filters(),
				new PropertyFilter(StoreSchema.DisplayableName, () -> DisplayableName),
				new PropertyFilter(StoreSchema.QueryableName, () -> QueryableName)
		);
	}
}
