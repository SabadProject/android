package farayan.sabad.models.GroupUnit

import com.j256.ormlite.dao.RuntimeExceptionDao
import farayan.commons.QueryBuilderCore.BaseParams
import farayan.commons.QueryBuilderCore.EntityFilter
import farayan.commons.QueryBuilderCore.PropertyValueConditionModes
import farayan.commons.QueryBuilderCore.RelationalOperators
import farayan.commons.QueryBuilderCore.SortConfig
import farayan.sabad.SabadTheApp
import farayan.sabad.core.OnePlace.Group.GroupEntity
import farayan.sabad.core.OnePlace.GroupUnit.GroupUnitEntity
import farayan.sabad.core.OnePlace.GroupUnit.GroupUnitParams
import farayan.sabad.core.OnePlace.GroupUnit.GroupUnitSchema
import farayan.sabad.core.OnePlace.GroupUnit.IGroupUnitRepo
import farayan.sabad.core.model.unit.UnitEntity

class GroupUnitRepo : IGroupUnitRepo {
    override fun DAO(): RuntimeExceptionDao<GroupUnitEntity, Int> {
        return SabadTheApp.DB().getRuntimeExceptionDao(GroupUnitEntity::class.java)
    }

    override fun ensureRelated(Group: GroupEntity, unit: UnitEntity): GroupUnitEntity {
        val params = GroupUnitParams()
        params.Group = EntityFilter(Group)
        params.Unit = EntityFilter(unit)
        var entity = First(params)
        if (entity != null) return entity
        entity = GroupUnitEntity(Group, unit, 0)
        Save(entity)
        return entity
    }

    override fun groupUnits(group: GroupEntity): List<GroupUnitEntity> {
        val groupUnits = All(GroupUnitParams().apply {
            Group = EntityFilter(
                group,
                RelationalOperators.Equal,
                PropertyValueConditionModes.ProvidedValueOrNull
            )
        }) ?: listOf()
        val defaultUnits = All(GroupUnitParams().apply {
            Group = EntityFilter(PropertyValueConditionModes.ProvidedValueOrNull)
            Sorts.add(0, SortConfig(GroupUnitSchema.Position))
        }) ?: listOf()
        return groupUnits.union(defaultUnits).toList()
    }

    override fun NewParams(): BaseParams<GroupUnitEntity> {
        return GroupUnitParams()
    }
}