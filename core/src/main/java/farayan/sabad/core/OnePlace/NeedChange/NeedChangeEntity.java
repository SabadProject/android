package farayan.sabad.core.OnePlace.NeedChange;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import farayan.sabad.core.OnePlace.Group.GroupEntity;
import farayan.sabad.core.base.SabadEntityBase;

@DatabaseTable(tableName = NeedChangeSchema.NeedChanges)
public class NeedChangeEntity extends SabadEntityBase<NeedChangeEntity>
{
	@DatabaseField(columnName = NeedChangeSchema.Group, foreign = true)
	public GroupEntity Group;

	@DatabaseField(columnName = NeedChangeSchema.Value)
	public boolean Value;

	@DatabaseField(columnName = NeedChangeSchema.Instant)
	public long Instant;

	public NeedChangeEntity() {

	}

	public NeedChangeEntity(GroupEntity Group, boolean value) {
		Group = Group;
		Value = value;
		Instant = System.currentTimeMillis();
	}

	@Override
	public boolean NeedsRefresh() {
		return Group == null;
	}

}
