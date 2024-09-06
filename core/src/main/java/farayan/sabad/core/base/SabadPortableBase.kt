package farayan.sabad.core.base

import farayan.commons.QueryBuilderCore.BasePortable
import farayan.commons.QueryBuilderCore.IEntity
import org.jetbrains.annotations.NotNull
import java.util.UUID

open class SabadPortableBase<TEntity : IEntity>(
        localID: Long,
        alwaysID: UUID,
        val GlobalID: Long?,
        val PendingSync: Boolean,
        val LastSynced: Long?,
) : BasePortable(localID, alwaysID) {
    constructor(entity: @NotNull SabadEntityBase<TEntity>) : this(
            entity.ID.toLong(),
            entity.AlwaysID,
            entity.GlobalID,
            entity.PendingSync,
            entity.LastSynced
    )
}