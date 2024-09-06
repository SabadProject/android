package farayan.sabad.utility

import farayan.commons.FarayanUtility

fun String?.isValuable(): Boolean {
    return this?.isNotBlank() == true
}

val String?.isUsable: Boolean
    get() = this?.isNotBlank() == true


fun String?.or(replacement: String): String {
    return if (this.isUsable) this!! else replacement
}

fun String.displayable(): String {
    return this.trim()
}

fun String.queryable(): String {
    return FarayanUtility
        .ConvertNumbersToAscii(this.trim())
        .replace("آ".toRegex(), "ا")
        .replace("ي".toRegex(), "ی")
        .replace("ك".toRegex(), "ک")
        .replace("ظ".toRegex(), "ز")
        .replace("ذ".toRegex(), "ز")
        .replace("ظ".toRegex(), "ز")
        .replace("ط".toRegex(), "ت")
        .replace("ح".toRegex(), "ه")
        .replace("ص".toRegex(), "س")
        .replace("ث".toRegex(), "س")
        .replace(" ".toRegex(), "")
        .replace("‌".toRegex(), "")
        .replace("ئ".toRegex(), "ی")
        .replace("غ".toRegex(), "ق")
}
