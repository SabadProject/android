package farayan.sabad.core.commons

import android.content.Context
import java.math.BigDecimal

data class Money(val amount: BigDecimal, val currency: Currency) {
    fun textual(ctx: Context): String {
        return currency.formatter(amount, ctx)
    }

    companion object {
        fun of(amount: String?, currency: String?): Money? {
            if (amount == null)
                return null
            if (currency == null)
                return null
            val parsedAmount = tryCatch({ BigDecimal(amount) }, null) ?: return null
            val parsedCurrency = currency.currency() ?: return null
            return Money(parsedAmount, parsedCurrency)
        }
    }
}