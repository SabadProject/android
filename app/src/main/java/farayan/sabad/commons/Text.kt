package farayan.sabad.commons

import farayan.commons.queryable
import farayan.sabad.utility.displayable

data class Text(val original: String, val displayable: String, val queryable: String) {
    fun equals(otherDisplayable: String, otherQueryableName: String): Boolean {
        return displayable.contentEquals(otherDisplayable) && queryable.contentEquals(otherQueryableName)
    }

    val isEmpty: Boolean
        get() = original.isEmpty() || displayable.isEmpty() || queryable.isEmpty()

    constructor(original: String) : this(original, original.displayable(), original.queryable())
}