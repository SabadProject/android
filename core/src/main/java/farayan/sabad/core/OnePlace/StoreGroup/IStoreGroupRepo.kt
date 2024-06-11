package farayan.sabad.core.OnePlace.StoreGroup

import farayan.commons.QueryBuilderCore.IRepo
import farayan.sabad.core.OnePlace.Group.GroupEntity
import farayan.sabad.core.OnePlace.Store.StoreEntity

interface IStoreGroupRepo : IRepo<StoreGroupEntity> {
    fun EnsureRelated(group: GroupEntity, store: StoreEntity): StoreGroupEntity
}