package farayan.sabad.core.OnePlace.NeedChange;

import org.apache.commons.lang3.ArrayUtils;

import farayan.commons.QueryBuilderCore.ComparableFilter;
import farayan.commons.QueryBuilderCore.EntityFilter;
import farayan.commons.QueryBuilderCore.PropertyFilter;
import farayan.sabad.core.OnePlace.Group.GroupEntity;
import farayan.sabad.core.base.SabadParamsBase;

public class NeedChangeParams extends SabadParamsBase<NeedChangeEntity>
{
	public EntityFilter<GroupEntity> Group;

	public ComparableFilter<Boolean> Value;

	public ComparableFilter<Long> Instant;

	@Override
	public PropertyFilter[] Filters() {
		return ArrayUtils.addAll(
				super.Filters(),
				new PropertyFilter(NeedChangeSchema.Group, () -> Group),
				new PropertyFilter(NeedChangeSchema.Value, () -> Value),
				new PropertyFilter(NeedChangeSchema.Instant, () -> Instant)
		);
	}
}
