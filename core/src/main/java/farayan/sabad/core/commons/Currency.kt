@file:Suppress("SpellCheckingInspection")

package farayan.sabad.core.commons

import android.content.Context
import farayan.sabad.core.R
import java.io.Serializable
import java.math.BigDecimal

enum class Currency(val precision: Int, val resourceId: Int, val formatter: (amount: BigDecimal, context: Context) -> String) : Serializable {
    Rial(0, R.string.currency_name_rial, { a, c -> "${a.toString().localize()} ${c.getString(R.string.currency_name_rial)}" }),
    Tuman(1, R.string.currency_name_1tuman, { a, c -> "${a.toString().localize()} ${c.getString(R.string.currency_name_1tuman)}" }),
    HezarTuman(3, R.string.currency_name_htuman, { a, c -> "${a.toString().localize()} ${c.getString(R.string.currency_name_htuman)}" }),
    MillionTuman(3, R.string.currency_name_mtuman, { a, c -> "${a.toString().localize()} ${c.getString(R.string.currency_name_mtuman)}" }),
    Euro(2, R.string.currency_name_euro, { a, c -> "$a${c.getString(R.string.currency_name_euro)}" }),
    Dollar(2, R.string.currency_name_dollar, { a, c -> "$a${c.getString(R.string.currency_name_dollar)}" }),

    ;

}