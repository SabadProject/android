@file:Suppress("SpellCheckingInspection")

package farayan.sabad.core.commons

import farayan.sabad.core.R
import java.io.Serializable

enum class Currency(val precision: Int, val resourceId: Int) : Serializable {
    Rial(0, R.string.currency_name_rial),
    Tuman(1, R.string.currency_name_1tuman),
    HezarTuman(3, R.string.currency_name_htuman),
    MillionTuman(3, R.string.currency_name_mtuman),
    Euro(2, R.string.currency_name_euro),
    Dollar(2, R.string.currency_name_dollar),

    ;

}