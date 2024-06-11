package farayan.sabad.models.StoreCategory

import com.j256.ormlite.dao.RuntimeExceptionDao
import farayan.commons.QueryBuilderCore.BaseParams
import farayan.commons.QueryBuilderCore.EntityFilter
import farayan.sabad.SabadTheApp
import farayan.sabad.core.OnePlace.Category.CategoryEntity
import farayan.sabad.core.OnePlace.Store.StoreEntity
import farayan.sabad.core.OnePlace.StoreCategory.IStoreCategoryRepo
import farayan.sabad.core.OnePlace.StoreCategory.StoreCategoryEntity
import farayan.sabad.core.OnePlace.StoreCategory.StoreCategoryParams

class StoreCategoryRepo : IStoreCategoryRepo {
    override fun DAO(): RuntimeExceptionDao<StoreCategoryEntity, Int> {
        return SabadTheApp.DB().getRuntimeExceptionDao(StoreCategoryEntity::class.java)
    }

    override fun NewParams(): BaseParams<StoreCategoryEntity> {
        return StoreCategoryParams();
    }

    override fun EnsureRelated(store: StoreEntity, category: CategoryEntity): StoreCategoryEntity {
        val params = StoreCategoryParams()
        params.Store = EntityFilter(store)
        params.Category = EntityFilter(category)
        var entity = First(params)
        if (entity != null) return entity
        entity = StoreCategoryEntity(store, category)
        Save(entity)
        return entity
    }
}