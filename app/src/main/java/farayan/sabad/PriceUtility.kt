package farayan.sabad

import java.math.BigDecimal
import java.math.RoundingMode

fun referencePrice(unitPrice: BigDecimal, unitQuantity: BigDecimal, unitCoefficient: BigDecimal): BigDecimal {
    return unitPrice.divide((unitQuantity * unitCoefficient), 2, RoundingMode.HALF_EVEN)
}
