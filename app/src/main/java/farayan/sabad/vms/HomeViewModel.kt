package farayan.sabad.vms

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import farayan.sabad.SabadDependencies
import farayan.sabad.db.Category
import farayan.sabad.db.Item
import farayan.sabad.db.Product
import farayan.sabad.repo.CategoryRepo
import farayan.sabad.repo.ItemRepo
import farayan.sabad.repo.ProductRepo
import farayan.sabad.repo.UnitRepo
import farayan.sabad.utility.isUsable
import farayan.sabad.utility.queryable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import farayan.sabad.db.Unit as PersistenceUnit

class HomeViewModel @Inject constructor(
    private val categoriesRepo: CategoryRepo,
    private val itemRepo: ItemRepo,
    private val productRepo: ProductRepo,
    private val unitRepo: UnitRepo,
) : ViewModel() {
    fun changeNeeded(category: Category, needed: Boolean) {
        val value = ArrayList(categories.value)
        value[value.indexOf(category)] = categoriesRepo.changeNeeded(category, needed)
        categories.value = value
    }

    fun filter(query: String) {
        categoriesFilterQuery = query.queryable()
        categories.value = if (categoriesFilterQuery.isUsable) categoriesRepo.filter("%$categoriesFilterQuery%") else categoriesRepo.all()
    }

    fun product(productId: Long): Product = productRepo.byId(productId)!!
    fun unit(unitId: Long): PersistenceUnit? = unitRepo.byId(unitId)
    fun removeItem(item: Item) {
        itemRepo.delete(item)
    }

    private var categoriesFilterQuery: String = ""
    val categories = MutableStateFlow(categoriesRepo.all())
    val pickedProducts = MutableStateFlow(productRepo.pickings())
    val pickedUnits = MutableStateFlow(unitRepo.pickings())
    val items = itemRepo.pickings(viewModelScope).onEach { items ->
        Log.d("flow", "Collected items in HomeViewModel: ${items.size}")
    }

    init {
        viewModelScope.launch {
            while (true) {
                Log.i("flow", "HomeViewModel: ${this@HomeViewModel}")
                delay(5_000)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {
                Log.i("flow", "creating HomeViewModel")
                val sd = SabadDependencies()

                return HomeViewModel(
                    sd.categoryRepo(),
                    sd.itemRepo(),
                    sd.productRepo(),
                    sd.unitRepo(),
                ) as T
            }
        }
    }
}