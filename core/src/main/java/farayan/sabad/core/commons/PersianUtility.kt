@file:Suppress("unused")

package farayan.sabad.core.commons


fun String.persianDigits(): String {
    var result = this
    for (index in 0 until 10) {
        result = result.replace(Char(48 + index), Char(1776 + index))
    }
    return result
}

fun String.ltr(): String = "\u200E${this}\u200F"
fun String.rtl(): String = "\u200F${this}\u200E"

fun String.localize(): String {
    val digitsOffset = 0x06F0 - '0'.code
    val sb = StringBuilder()

    for (c in this) {
        if (c in '0'..'9') {
            sb.append(c + digitsOffset)
        } else {
            sb.append(c)
        }
    }
    return sb.toString()
}

fun String.globalizeNumbers(): String {
    val digitsOffset = 0x06F0 - '0'.code
    val sb = StringBuilder()

    for (c in this) {
        if (c in '۰'..'۹') {
            sb.append(c - digitsOffset)
        } else {
            sb.append(c)
        }
    }
    return sb.toString()
}