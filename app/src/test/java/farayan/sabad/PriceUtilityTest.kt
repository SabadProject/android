package farayan.sabad

import farayan.sabad.utility.referencePrice
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.math.BigDecimal

class PriceUtilityTest {

    @Test
    fun `when a bottle of half liter costs 10 euros, then one liter of wine must cost 20 euros`() {
        assertEquals(BigDecimal("20.00"), referencePrice(BigDecimal("10.00"), BigDecimal("0.5"), BigDecimal("1.00")))
    }

    @Test
    fun `when a bottle of 500 milliliter costs 10 euros, then one liter of wine must cost 20 euros`() {
        assertEquals(BigDecimal("20.00"), referencePrice(BigDecimal("10.00"), BigDecimal("500"), BigDecimal("0.001")))
    }

    @Test
    fun `when a package of 120 gram nuts costs 3,14 euros, then one kg of nuts must costs 28,33 euros`() {
        assertEquals(BigDecimal("26.17"), referencePrice(BigDecimal("3.14"), BigDecimal("120"), BigDecimal("0.001")))
    }

    @Test
    fun `when scale of different values are different, then fix all scales to 2`() {
        assertEquals(BigDecimal("2.50"), referencePrice(BigDecimal("5"), BigDecimal("2"), BigDecimal("1.0")))
    }
}