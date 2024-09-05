package farayan.sabad.commons

import farayan.sabad.utility.hasValue
import kotlinx.coroutines.flow.MutableStateFlow

class Fixable<T>(val value: T? = null, val fixed: Boolean = false) {
    operator fun invoke() = value
}
val <T> MutableStateFlow<Fixable<T>>.hasFixedValue: Boolean
    get() {
        return this.value.hasFixedValue
    }

val <T> MutableStateFlow<Fixable<T>>.hasAnyValue: Boolean
    get() {
        return this.value.hasAnyValue
    }

val <T> MutableStateFlow<Fixable<T>>.theValue: T
    get() {
        return this.value.theValue
    }

val <T> MutableStateFlow<Fixable<T>>.anyValue: T?
    get() {
        return this.value.anyValue
    }

val <T> Fixable<T>.hasFixedValue: Boolean
    get() {
        return this.value.hasValue && this.fixed
    }

val <T> Fixable<T>.hasAnyValue: Boolean
    get() {
        return this.value.hasValue
    }

val <T> Fixable<T>.theValue: T
    get() {
        return this.value ?: throw RuntimeException("value is null")
    }

val <T> Fixable<T>.anyValue: T?
    get() {
        return this.value
    }