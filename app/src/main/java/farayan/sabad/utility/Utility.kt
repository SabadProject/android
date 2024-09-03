package farayan.sabad.utility

val Any?.hasValue: Boolean
    get() {
        return this != null
    }

operator fun <T> Boolean.invoke(onTrue: T, onFalse: T): T {
    return if (this) onTrue else onFalse
}

operator fun <T> Boolean.invoke(onTrue: () -> T, onFalse: () -> T): T {
    return if (this) onTrue() else onFalse()
}

fun <T> tryCatch(action: () -> T, default: T): T {
    return try {
        action()
    } catch (e: Exception) {
        default
    }
}