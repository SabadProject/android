package farayan.sabad.models.Unit

import com.j256.ormlite.dao.RuntimeExceptionDao
import farayan.commons.QueryBuilderCore.BaseParams
import farayan.sabad.SabadTheApp
import farayan.sabad.core.OnePlace.Unit.IUnitRepo
import farayan.sabad.core.OnePlace.Unit.UnitParams
import farayan.sabad.core.model.unit.UnitEntity

class UnitRepo : IUnitRepo {
    override fun DAO(): RuntimeExceptionDao<UnitEntity, Int> {
        return SabadTheApp.DB().getRuntimeExceptionDao(UnitEntity::class.java)
    }

    override fun NewParams(): BaseParams<UnitEntity> {
        return UnitParams()
    }
}