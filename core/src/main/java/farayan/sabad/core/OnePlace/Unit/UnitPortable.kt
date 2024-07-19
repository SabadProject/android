package farayan.sabad.core.OnePlace.Unit

import farayan.sabad.core.base.SabadPortableBase
import farayan.sabad.core.commons.UnitVariations
import farayan.sabad.core.model.unit.UnitEntity
import org.jetbrains.annotations.NotNull

class UnitPortable(
    unit: UnitEntity,
    val displayableName: String?,
    val queryableName: String?,
    val category: String?,
    val variation: UnitVariations?
) : SabadPortableBase<UnitEntity>(unit) {
    constructor(unit: @NotNull UnitEntity) : this(
        unit,
        unit.displayableName,
        unit.queryableName,
        unit.category,
        unit.variation
    )
}