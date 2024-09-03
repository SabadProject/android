package farayan.sabad.commons

import farayan.commons.queryable
import farayan.sabad.utility.displayable

data class Text(val original: String, val displayable: String, val queryable: String) {
    val isEmpty: Boolean
        get() = original.isEmpty() || displayable.isEmpty() || queryable.isEmpty()

    constructor(original: String) : this(original, original.displayable(), original.queryable())
}