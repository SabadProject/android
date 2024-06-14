package farayan.sabad.models.GroupUnit

import com.j256.ormlite.dao.RuntimeExceptionDao
import farayan.commons.QueryBuilderCore.BaseParams
import farayan.commons.QueryBuilderCore.EntityFilter
import farayan.sabad.SabadTheApp
import farayan.sabad.core.OnePlace.Group.GroupEntity
import farayan.sabad.core.OnePlace.GroupUnit.GroupUnitEntity
import farayan.sabad.core.OnePlace.GroupUnit.GroupUnitParams
import farayan.sabad.core.OnePlace.GroupUnit.IGroupUnitRepo
import farayan.sabad.core.OnePlace.Unit.UnitEntity

class GroupUnitRepo : IGroupUnitRepo {
    override fun DAO(): RuntimeExceptionDao<GroupUnitEntity, Int> {
        return SabadTheApp.DB().getRuntimeExceptionDao(GroupUnitEntity::class.java)
    }

    override fun EnsureRelated(Group: GroupEntity, unit: UnitEntity): GroupUnitEntity {
        val params = GroupUnitParams()
        params.Group = EntityFilter(Group)
        params.Unit = EntityFilter(unit)
        var entity = First(params)
        if (entity != null) return entity
        entity = GroupUnitEntity(Group, unit)
        Save(entity)
        return entity
    }

    override fun NewParams(): BaseParams<GroupUnitEntity> {
        return GroupUnitParams()
    }
}