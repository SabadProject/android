package farayan.sabad.core.OnePlace.StoreGroup;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import farayan.sabad.core.OnePlace.Group.GroupEntity;
import farayan.sabad.core.OnePlace.Store.StoreEntity;
import farayan.sabad.core.base.SabadEntityBase;

@DatabaseTable(tableName = StoreGroupSchema.StoreGroups)
public class StoreGroupEntity extends SabadEntityBase<StoreGroupEntity>
{
	@DatabaseField(columnName = StoreGroupSchema.Store, foreign = true)
	public StoreEntity Store;

	@DatabaseField(columnName = StoreGroupSchema.Group, foreign = true)
	public GroupEntity Group;

	public StoreGroupEntity() {

	}

	public StoreGroupEntity(StoreEntity store, GroupEntity Group) {
		Store = store;
		Group = Group;
	}

	@Override
	public boolean NeedsRefresh() {
		return Store == null;
	}
}
