package farayan.sabad.vm

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomepageViewModel : ViewModel() {
    private val ordersButton = TopAppBarIcon(Icons.Filled.Menu, "list", { navController -> navController.navigate("/orders") }, 0)

    private val actionsMutable = MutableStateFlow(listOf(ordersButton))
    val actionsReadOnly = actionsMutable.asStateFlow()
    fun changeActions(icons: List<TopAppBarIcon>) {
        actionsMutable.value = icons + ordersButton
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

data class TopAppBarIcon(val icon: ImageVector, val name: String, val onClick: (navController: NavHostController) -> Unit, val position: Int);