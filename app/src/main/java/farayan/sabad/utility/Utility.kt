package farayan.sabad.utility

import android.app.Dialog
import android.view.Gravity
import android.view.WindowManager

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


fun Dialog.maximize() {
    val window = window
    val wlp = window!!.attributes
    wlp.gravity = Gravity.CENTER
    window.attributes = wlp
    getWindow()!!.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.MATCH_PARENT
    )
}