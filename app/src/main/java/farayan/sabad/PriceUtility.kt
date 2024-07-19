package farayan.sabad

import java.math.BigDecimal
import java.math.RoundingMode

fun referencePrice(unitPrice: BigDecimal, unitQuantity: BigDecimal, unitCoefficient: BigDecimal): BigDecimal? {
    val divideBy = unitQuantity * unitCoefficient
    if (divideBy <= BigDecimal.ZERO)
        return null
    return unitPrice.divide(divideBy, 2, RoundingMode.HALF_EVEN)
}
