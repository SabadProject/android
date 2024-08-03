package farayan.sabad.core.OnePlace.Group

import farayan.commons.QueryBuilderCore.IRepo
import farayan.sabad.core.OnePlace.GroupUnit.IGroupUnitRepo
import farayan.sabad.core.model.product.IProductRepo
import javax.annotation.Nonnull

interface IGroupRepo : IRepo<GroupEntity> {
    fun New(@Nonnull name: String, description: String?, icon: String?, undelete: Boolean): GroupEntity
    fun Rename(Group: GroupEntity, @Nonnull name: String)
    fun Remove(
        productRepo: IProductRepo,
        GroupUnitRepo: IGroupUnitRepo,
        Group: GroupEntity
    )

    fun FirstByName(@Nonnull name: String): GroupEntity?
}