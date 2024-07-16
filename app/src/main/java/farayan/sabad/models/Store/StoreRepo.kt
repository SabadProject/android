package farayan.sabad.models.Store

import com.j256.ormlite.dao.RuntimeExceptionDao
import farayan.commons.QueryBuilderCore.BaseParams
import farayan.sabad.SabadTheApp
import farayan.sabad.core.OnePlace.Store.IStoreRepo
import farayan.sabad.core.OnePlace.Store.StoreEntity
import farayan.sabad.core.OnePlace.Store.StoreParams

class StoreRepo : IStoreRepo {
    override fun DAO(): RuntimeExceptionDao<StoreEntity, Int> {
        return SabadTheApp.DB().getRuntimeExceptionDao(StoreEntity::class.java)
    }

    override fun NewParams(): BaseParams<StoreEntity> {
        return StoreParams()
    }
}