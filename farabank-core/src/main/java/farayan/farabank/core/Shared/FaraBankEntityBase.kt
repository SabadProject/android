package farayan.farabank.core.Shared

import com.j256.ormlite.dao.RuntimeExceptionDao
import farayan.commons.QueryBuilderCore.BaseEntity
import farayan.commons.QueryBuilderCore.IEntity

abstract class FaraBankEntityBase<TEntity : IEntity> : BaseEntity<TEntity>() {

    override fun NeedsRefresh(): Boolean {
        return false
    }

    override fun DAO(): RuntimeExceptionDao<TEntity, Int>? {
        return null
    }
}