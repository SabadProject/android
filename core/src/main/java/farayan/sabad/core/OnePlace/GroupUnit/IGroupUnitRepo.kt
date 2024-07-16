package farayan.sabad.core.OnePlace.GroupUnit

import farayan.commons.QueryBuilderCore.IRepo
import farayan.sabad.core.OnePlace.Group.GroupEntity
import farayan.sabad.core.model.unit.UnitEntity

interface IGroupUnitRepo : IRepo<GroupUnitEntity> {
    fun ensureRelated(group: GroupEntity, unit: UnitEntity): GroupUnitEntity
    fun groupUnits(group: GroupEntity): List<GroupUnitEntity>
}