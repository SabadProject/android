package farayan.sabad.commons

import android.content.Context

data class ConditionalErrorMessage(val value: String, val errorResId: Int) {
    fun errorMessage(currentValue: String, context: Context): String? {
        return if (currentValue.contentEquals(value, true))
            context.getString(errorResId)
        else
            null
    }
}