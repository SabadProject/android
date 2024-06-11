package farayan.sabad.core.OnePlace.NeedChange

import farayan.commons.QueryBuilderCore.IRepo
import farayan.sabad.core.OnePlace.Group.GroupEntity

interface INeedChangeRepo : IRepo<NeedChangeEntity> {
    fun RegisterChange(Group: GroupEntity, value: Boolean)
}