package farayan.sabad.core.OnePlace.GroupUnit;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import farayan.sabad.core.OnePlace.Group.GroupEntity;
import farayan.sabad.core.base.SabadEntityBase;
import farayan.sabad.core.model.unit.UnitEntity;

@DatabaseTable(tableName = GroupUnitSchema.GroupUnits)
public class GroupUnitEntity extends SabadEntityBase<GroupUnitEntity> {
    @DatabaseField(columnName = GroupUnitSchema.Group, foreign = true)
    public GroupEntity Group;

    @DatabaseField(columnName = GroupUnitSchema.Unit, foreign = true)
    public UnitEntity Unit;

    public GroupUnitEntity() {
    }

    public GroupUnitEntity(GroupEntity group, UnitEntity unit) {
        Group = group;
        Unit = unit;
    }

    @Override
    public boolean NeedsRefresh() {
        return Group == null;
    }
}
