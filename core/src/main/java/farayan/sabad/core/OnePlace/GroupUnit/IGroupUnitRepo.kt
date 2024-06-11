package farayan.sabad.core.OnePlace.GroupUnit

import farayan.commons.QueryBuilderCore.IRepo
import farayan.sabad.core.OnePlace.Group.GroupEntity
import farayan.sabad.core.OnePlace.Unit.UnitEntity

interface IGroupUnitRepo : IRepo<GroupUnitEntity> {
    fun EnsureRelated(Group: GroupEntity, unit: UnitEntity): GroupUnitEntity
}