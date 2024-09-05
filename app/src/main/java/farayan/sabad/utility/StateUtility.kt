package farayan.sabad.utility

import kotlinx.coroutines.flow.MutableStateFlow


fun <T> MutableStateFlow<T>.invoke(): T {
    return this.value
}