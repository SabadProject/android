package farayan.sabad.core.base;

import org.apache.commons.lang3.ArrayUtils;

import farayan.commons.QueryBuilderCore.BaseParams;
import farayan.commons.QueryBuilderCore.ComparableFilter;
import farayan.commons.QueryBuilderCore.PropertyFilter;

public class SabadParamsBase<TEntity extends SabadEntityBase<TEntity>> extends BaseParams<TEntity>
{
	public ComparableFilter<Long> GlobalID;
	public ComparableFilter<Boolean> PendingSync;
	public ComparableFilter<Long> LastSynced;

	@Override
	public PropertyFilter[] Filters() {
		return ArrayUtils.addAll(
				super.Filters(),
				new PropertyFilter(SabadSchemaBase.GlobalID, () -> GlobalID),
				new PropertyFilter(SabadSchemaBase.PendingSync, () -> PendingSync),
				new PropertyFilter(SabadSchemaBase.LastSynced, () -> LastSynced)
		);
	}
}
