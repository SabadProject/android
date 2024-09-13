package farayan.sabad.vm

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomepageViewModel : ViewModel() {
    private val actionsMutable = MutableStateFlow(listOf<TopAppBarIcon>())
    val actionsReadOnly = actionsMutable.asStateFlow()
    fun changeActions(icons: List<TopAppBarIcon>) {
        actionsMutable.value = icons
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {
                return HomepageViewModel(
                ) as T
            }
        }
    }
}

data class TopAppBarIcon(val icon: ImageVector, val name: String, val position: Int, val onClick: () -> Unit);