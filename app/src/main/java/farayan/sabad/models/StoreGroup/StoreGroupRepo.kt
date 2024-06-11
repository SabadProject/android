package farayan.sabad.models.StoreGroup

import com.j256.ormlite.dao.RuntimeExceptionDao
import farayan.commons.QueryBuilderCore.BaseParams
import farayan.commons.QueryBuilderCore.EntityFilter
import farayan.sabad.SabadTheApp
import farayan.sabad.core.OnePlace.Group.GroupEntity
import farayan.sabad.core.OnePlace.Store.StoreEntity
import farayan.sabad.core.OnePlace.StoreGroup.IStoreGroupRepo
import farayan.sabad.core.OnePlace.StoreGroup.StoreGroupEntity
import farayan.sabad.core.OnePlace.StoreGroup.StoreGroupParams

class StoreGroupRepo : IStoreGroupRepo {
    override fun DAO(): RuntimeExceptionDao<StoreGroupEntity, Int> {
        return SabadTheApp.DB().getRuntimeExceptionDao(StoreGroupEntity::class.java)
    }

    override fun NewParams(): BaseParams<StoreGroupEntity> {
        return StoreGroupParams();
    }

    override fun EnsureRelated(Group: GroupEntity, store: StoreEntity): StoreGroupEntity {
        val params = StoreGroupParams()
        params.Store = EntityFilter(store)
        params.Group = EntityFilter(Group)
        var entity = First(params)
        if (entity != null) return entity
        entity = StoreGroupEntity(store, Group)
        Save(entity)
        return entity
    }
}