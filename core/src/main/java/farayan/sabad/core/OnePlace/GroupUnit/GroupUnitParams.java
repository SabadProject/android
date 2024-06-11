package farayan.sabad.core.OnePlace.GroupUnit;

import org.apache.commons.lang3.ArrayUtils;

import farayan.commons.QueryBuilderCore.EntityFilter;
import farayan.commons.QueryBuilderCore.PropertyFilter;
import farayan.sabad.core.OnePlace.Group.GroupEntity;
import farayan.sabad.core.OnePlace.Unit.UnitEntity;
import farayan.sabad.core.base.SabadParamsBase;

public class GroupUnitParams extends SabadParamsBase<GroupUnitEntity>
{
	public EntityFilter<GroupEntity> Group;
	public EntityFilter<UnitEntity> Unit;

	@Override
	public PropertyFilter[] Filters() {
		return ArrayUtils.addAll(
				super.Filters(),
				new PropertyFilter(GroupUnitSchema.Group, () -> Group),
				new PropertyFilter(GroupUnitSchema.Unit, () -> Unit)
		);
	}
}
