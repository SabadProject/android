package farayan.sabad.core.OnePlace.Unit

import farayan.sabad.core.base.SabadPortableBase
import org.jetbrains.annotations.NotNull

class UnitPortable(
        unit: UnitEntity,
        val DisplayableName: String?,
        val QueryableName: String?,
        val Category: String?,
        val Coefficient: Double?,
) : SabadPortableBase<UnitEntity>(unit) {
    constructor(unit: @NotNull UnitEntity) : this(
            unit,
            unit.DisplayableName,
            unit.QueryableName,
            unit.Category,
            unit.Coefficient
    )
}