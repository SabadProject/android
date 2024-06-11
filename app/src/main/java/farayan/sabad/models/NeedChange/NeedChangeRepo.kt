package farayan.sabad.models.NeedChange

import com.j256.ormlite.dao.RuntimeExceptionDao
import farayan.commons.QueryBuilderCore.BaseParams
import farayan.sabad.SabadTheApp
import farayan.sabad.core.OnePlace.Group.GroupEntity
import farayan.sabad.core.OnePlace.NeedChange.INeedChangeRepo
import farayan.sabad.core.OnePlace.NeedChange.NeedChangeEntity
import farayan.sabad.core.OnePlace.NeedChange.NeedChangeParams

class NeedChangeRepo : INeedChangeRepo {
    override fun DAO(): RuntimeExceptionDao<NeedChangeEntity, Int> {
        return SabadTheApp.DB().getRuntimeExceptionDao(NeedChangeEntity::class.java)
    }

    override fun NewParams(): BaseParams<NeedChangeEntity> {
        return NeedChangeParams();
    }

    override fun RegisterChange(Group: GroupEntity, value: Boolean) {
        val change = NeedChangeEntity(Group, value)
        Save(change)
    }
}