package farayan.sabad.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import farayan.sabad.SabadDependencies
import farayan.sabad.db.Category
import farayan.sabad.isUsable
import farayan.sabad.queryable
import farayan.sabad.repo.CategoryRepo
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val categoriesRepo: CategoryRepo
) : ViewModel() {
    fun changeNeeded(category: Category, needed: Boolean) {
        val value = ArrayList(categories.value)
        value[value.indexOf(category)] = categoriesRepo.changeNeeded(category, needed)
        categories.value = value
    }

    fun filter(query: String) {
        categoriesFilterQuery = query.queryable()
        categories.value = if (categoriesFilterQuery.isUsable()) categoriesRepo.filter("%$categoriesFilterQuery%") else categoriesRepo.all()
    }

    private var categoriesFilterQuery: String = ""
    val categories = MutableStateFlow(categoriesRepo.all())

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {
                val sd = SabadDependencies()

                return HomeViewModel(
                    sd.categoryRepo(),
                ) as T
            }
        }
    }
}