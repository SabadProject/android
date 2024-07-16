package farayan.sabad.ui.components

import android.util.Log
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import java.math.BigDecimal


fun decimalPointPosition(value: String): Int {
    return value.indexOfFirst { c -> c == '.' }
}

fun BigDecimal.displayable(
    suffixedWithDecimalPoint: Boolean,
): TextFieldValue {
    val toString = this.toString()
    val decimalPointPos = decimalPointPosition(toString)
    val precision = if (decimalPointPos >= 0) toString.length - decimalPointPos - 1 else 0
    val format = "%,.${precision}f"
    val text = String.format(format, this).let { if (suffixedWithDecimalPoint) "$it." else it }
    return TextFieldValue(text, selection = TextRange(text.length))
}

fun parseBigDecimal(value: String): BigDecimal? {
    return try {
        val sb = StringBuilder()
        var decimalPointAdded = false
        for (c in value) {
            if (c in '0'..'9') {
                sb.append(c)
                continue
            }
            if (c == '.' && !decimalPointAdded) {
                sb.append('.')
                decimalPointAdded = true
                continue
            }
        }
        val bigDecimal = BigDecimal(sb.toString())
        bigDecimal
    } catch (e: Exception) {
        Log.i("number", "unable to parse value of $value")
        null
    }
}