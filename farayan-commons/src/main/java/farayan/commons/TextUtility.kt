package farayan.commons

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