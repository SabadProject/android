package farayan.sabad.core.OnePlace.Store

import farayan.sabad.core.base.SabadPortableBase
import org.jetbrains.annotations.NotNull

class StorePortable(
        store: StoreEntity,
        val DisplayableName: String,
        val QueryableName: String,
) : SabadPortableBase<StoreEntity>(store) {
    constructor(store: @NotNull StoreEntity) : this(
            store,
            store.DisplayableName,
            store.QueryableName,
    )
}