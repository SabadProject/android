package farayan.commons

import farayan.commons.components.NumberEntry
import java.text.NumberFormat

enum class SurroundTypes {
    None,
    LTR,
    RTL
}

fun Number.MoneyFormatted(localizer: NumberEntry.ILocalNumberProvider? = NumberEntry.PersianLocalNumberProvider(), surround: SurroundTypes = SurroundTypes.None): String {
    val formatted = NumberFormat.getInstance().format(this)
    val localized = localizer?.localize(formatted) ?: formatted
    return when (surround) {
        SurroundTypes.None -> localized
        SurroundTypes.LTR -> localized
        SurroundTypes.RTL -> localized
    }
}

class ExtensionMethods {
}