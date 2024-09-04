package farayan.sabad.vms

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import farayan.sabad.R
import farayan.sabad.SabadDeps
import farayan.sabad.commons.ConditionalErrorMessage
import farayan.sabad.commons.InvoiceSummary
import farayan.sabad.commons.Text
import farayan.sabad.core.commons.Currency
import farayan.sabad.core.commons.Money
import farayan.sabad.db.Category
import farayan.sabad.db.Item
import farayan.sabad.db.Product
import farayan.sabad.repo.CategoryRepo
import farayan.sabad.repo.ItemRepo
import farayan.sabad.repo.ProductRepo
import farayan.sabad.repo.UnitRepo
import farayan.sabad.utility.hasValue
import farayan.sabad.utility.invoke
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject
import farayan.sabad.db.Unit as PersistenceUnit

class HomeViewModel @Inject constructor(
    private val categoryRepo: CategoryRepo,
    private val itemRepo: ItemRepo,
    private val productRepo: ProductRepo,
    private val unitRepo: UnitRepo,
) : ViewModel() {
    fun changeNeeded(category: Category, needed: Boolean) {
        //val value = ArrayList(categories.value)
        /*value[value.indexOf(category)] =*/ categoryRepo.changeNeeded(category, needed)
        /*categories.value = value*/
    }

    fun refresh() {
        filter(queryReadOnly.value.original)
        refreshingMutable.value = false
    }

    fun filter(query: String) {
        this.queryMutable.value = Text(query)
    }

    fun product(productId: Long): Product = productRepo.byId(productId)!!
    fun unit(unitId: Long): PersistenceUnit? = unitRepo.byId(unitId)
    fun removeItem(item: Item) {
        itemRepo.delete(item)
    }

    fun invoiceSummary(): InvoiceSummary {
        val itemsSummary = itemRepo.pickingSummary() ?: return InvoiceSummary(
            0,
            categoryRepo.remainingCount(),
            0,
            null
        )
        return InvoiceSummary(
            categoryRepo.pickedCount(),
            categoryRepo.remainingCount(),
            itemsSummary.totalSum?.toLong() ?: 0,
            (itemsSummary.totalSum.hasValue && itemsSummary.currency.hasValue)(
                { Money(BigDecimal.valueOf(itemsSummary.totalSum ?: 0.0), Currency.valueOf(itemsSummary.currency!!)) },
                { null }
            )
        )
    }

    fun addCategory() {
        if (queryReadOnly.value.isEmpty)
            return
        val current = categoryRepo.byName(queryReadOnly.value)
        if (current.hasValue)
            return
        val created = categoryRepo.create(queryReadOnly.value)
        /*categories.value += created*/
    }

    fun createItem(category: Category) {
        itemRepo.ensure(productRepo.ensure(category, "PRODUCT1"), BigDecimal("17.0"), Currency.Euro, BigDecimal("1"), unitRepo.all().first(), null, null)
    }

    fun select(category: Category) {
        if (!selectedCategoriesMutable.value.contains(category)) {
            selectedCategoriesMutable.value += category
        }
    }

    fun unselect(category: Category) {
        if (selectedCategoriesMutable.value.contains(category)) {
            selectedCategoriesMutable.value -= category
        }
    }

    fun persistCategory(name: String, editingCategory: Category): Boolean {
        if (name.isEmpty())
            return false
        val current = categoryRepo.byName(name)
        if (current.hasValue && current!!.id != editingCategory.id) {
            editingCategoryErrorMutable.value = ConditionalErrorMessage(name, R.string.category_edit_duplicate_name_error)
            return false
        }
        categoryRepo.update(editingCategory, name)
        return true
    }

    fun deleteCategories(removingCategories: List<Category>): Boolean {
        if(removingCategories.isEmpty())
            return false
        categoryRepo.delete(removingCategories)
        return true
    }

    private val refreshingMutable = MutableStateFlow(false)
    val refreshingReadOnly = refreshingMutable.asStateFlow()
    private val queryMutable = MutableStateFlow(Text(""))
    val queryReadOnly = queryMutable.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val categories = queryReadOnly.flatMapLatest { if (it.isEmpty) categoryRepo.allFlow() else categoryRepo.filterFlow("%${it.queryable}%") }
    val pickedProducts = productRepo.pickingsFlow(viewModelScope)
    val pickedUnits = unitRepo.pickingsFlow(viewModelScope)
    val pickedItems = itemRepo.pickings(viewModelScope)
    private val selectedCategoriesMutable = MutableStateFlow(listOf<Category>())
    val selectedCategoriesReadonly = selectedCategoriesMutable.asStateFlow()
    private val editingCategoryErrorMutable = MutableStateFlow<ConditionalErrorMessage?>(null)
    val editingCategoryErrorReadOnly = editingCategoryErrorMutable.asStateFlow()

    init {
        viewModelScope.launch {
            Log.i("flow", "HomeViewModel: pickedItems: $pickedItems, itemRepo: $itemRepo")
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {
                Log.i("flow", "creating HomeViewModel")

                return HomeViewModel(
                    SabadDeps.categoryRepo(),
                    SabadDeps.itemRepo(),
                    SabadDeps.productRepo(),
                    SabadDeps.unitRepo(),
                ) as T
            }
        }
    }
}