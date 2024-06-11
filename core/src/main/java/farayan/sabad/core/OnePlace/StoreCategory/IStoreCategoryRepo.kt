package farayan.sabad.core.OnePlace.StoreCategory

import farayan.commons.QueryBuilderCore.IRepo
import farayan.sabad.core.OnePlace.Category.CategoryEntity
import farayan.sabad.core.OnePlace.Store.StoreEntity

interface IStoreCategoryRepo : IRepo<StoreCategoryEntity> {
    fun EnsureRelated(store: StoreEntity, category: CategoryEntity): StoreCategoryEntity
}