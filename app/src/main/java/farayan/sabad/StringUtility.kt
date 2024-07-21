package farayan.sabad

fun String?.isUsable(): Boolean {
    return this?.isNotBlank() == true
}