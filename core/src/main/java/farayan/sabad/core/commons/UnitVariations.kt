package farayan.sabad.core.commons

import farayan.sabad.core.R
import java.math.BigDecimal

@Suppress("unused")
enum class UnitVariations(
    val nameResId: Int,
    val coefficient: BigDecimal,
    val mainUnitNameResId: Int
) {
    g(R.string.unit_variation_g_name, BigDecimal("0.001"), R.string.unit_variation_kg_name),
    kg(R.string.unit_variation_kg_name, BigDecimal("1.0"), R.string.unit_variation_kg_name),
    ml(R.string.unit_variation_ml_name, BigDecimal("0.001"), R.string.unit_variation_l_name),
    l(R.string.unit_variation_l_name, BigDecimal("1.0"), R.string.unit_variation_l_name),
    p(R.string.unit_variation_p_name, BigDecimal("1.0"), R.string.unit_variation_p_name),
    ;
}