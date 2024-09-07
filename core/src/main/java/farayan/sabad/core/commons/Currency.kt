@file:Suppress("SpellCheckingInspection")

package farayan.sabad.core.commons

import android.content.Context
import farayan.sabad.core.R
import java.io.Serializable
import java.math.BigDecimal

enum class Currency(val precision: Int, val resourceId: Int, val formatter: (amount: BigDecimal, context: Context) -> String) : Serializable {
    Rial(0, R.string.currency_name_rial, { amount, context -> "${amount.toString().localize()} ${context.getString(R.string.currency_name_rial)}" }),
    Tuman(1, R.string.currency_name_1tuman, { amount, context -> "${amount.toString().localize()} ${context.getString(R.string.currency_name_1tuman)}" }),
    HezarTuman(3, R.string.currency_name_htuman, { amount, context -> "${amount.toString().localize()} ${context.getString(R.string.currency_name_htuman)}" }),
    MillionTuman(3, R.string.currency_name_mtuman, { amount, context -> "${amount.setScale(2).toString().localize()} ${context.getString(R.string.currency_name_mtuman)}" }),
    Euro(2, R.string.currency_name_euro, { amount, context -> "${amount.setScale(2, java.math.RoundingMode.HALF_UP)}${context.getString(R.string.currency_name_euro)}" }),
    Dollar(2, R.string.currency_name_dollar, { amount, context -> "$amount${context.getString(R.string.currency_name_dollar)}" }),

    ;
}

fun String.currency(): Currency? {
    return try {
        Currency.valueOf(this)
    } catch (e: Exception) {
        null
    }
}