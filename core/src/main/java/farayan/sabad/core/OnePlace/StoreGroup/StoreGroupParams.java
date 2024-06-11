package farayan.sabad.core.OnePlace.StoreGroup;

import org.apache.commons.lang3.ArrayUtils;

import farayan.commons.QueryBuilderCore.EntityFilter;
import farayan.commons.QueryBuilderCore.PropertyFilter;
import farayan.sabad.core.OnePlace.Store.StoreEntity;
import farayan.sabad.core.OnePlace.Group.GroupEntity;
import farayan.sabad.core.base.SabadParamsBase;

public class StoreGroupParams extends SabadParamsBase<StoreGroupEntity>
{
	public EntityFilter<StoreEntity> Store;

	public EntityFilter<GroupEntity> Group;

	@Override
	public PropertyFilter[] Filters() {
		return ArrayUtils.addAll(
				super.Filters(),
				new PropertyFilter(StoreGroupSchema.Store, () -> Store),
				new PropertyFilter(StoreGroupSchema.Group, () -> Group)
		);
	}
}
