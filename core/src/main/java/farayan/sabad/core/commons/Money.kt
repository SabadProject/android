package farayan.sabad.core.commons

import android.content.Context
import java.math.BigDecimal

data class Money(val amount: BigDecimal, val currency: Currency) {
    fun textual(ctx: Context): String {
        return currency.formatter(amount, ctx)
    }
}