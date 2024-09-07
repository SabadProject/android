package farayan.sabad.vm

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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import java.math.BigDecimal
import javax.inject.Inject
import farayan.sabad.db.Unit as PersistenceUnit

class HomeViewModel @Inject constructor(
    private val categoryRepo: CategoryRepo,
    private val itemRepo: ItemRepo,
    private val productRepo: ProductRepo,
    private val unitRepo: UnitRepo,
) : ViewModel() {
    fun changeNeeded(category: Category, needed: Boolean): Category {
        skipThisUpdate = true
        return categoryRepo.changeNeeded(category, needed)
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

    fun addCategory(): Category? {
        if (queryReadOnly.value.isEmpty)
            return null
        val current = categoryRepo.byName(queryReadOnly.value)
        if (current.hasValue)
            return null
        val created = categoryRepo.create(queryReadOnly.value)
        return created
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

    fun editCategory(name: String, editingCategory: Category): Boolean {
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
        if (removingCategories.isEmpty())
            return false
        categoryRepo.delete(removingCategories)
        return true
    }

    fun clearSelection() {
        selectedCategoriesMutable.value = listOf()
        editingCategoryErrorMutable.value = null
    }

    private val refreshingMutable = MutableStateFlow(false)
    val refreshingReadOnly = refreshingMutable.asStateFlow()
    private val queryMutable = MutableStateFlow(Text(""))
    val queryReadOnly = queryMutable.asStateFlow()

    private var skipThisUpdate = false

    @OptIn(ExperimentalCoroutinesApi::class)
    val categories = queryReadOnly.flatMapLatest { if (it.isEmpty) categoryRepo.allFlow() else categoryRepo.filterFlow("%${it.queryable}%") }
        .distinctUntilChanged { _, _ ->
            if (skipThisUpdate) {
                skipThisUpdate = false
                true
            } else false
        }
    val pickedProducts = productRepo.pickingsFlow(viewModelScope)
    val pickedUnits = unitRepo.pickingsFlow(viewModelScope)
    val pickedItems = itemRepo.pickings(viewModelScope)

    data class CategorySummaryReport(val remainedCategoriesCount: Long, val pickedCategoriesCount: Long)

    val invoiceSummary = categoryRepo
        .remainedCategoriesCountFlow
        .combine(categoryRepo.pickedCategoriesCountFlow) { r, p -> CategorySummaryReport(r, p) }
        .combine(itemRepo.itemSummary) { categorySummaryReport, itemSummaryReport ->
            InvoiceSummary(
                categorySummaryReport.pickedCategoriesCount,
                categorySummaryReport.remainedCategoriesCount,
                itemSummaryReport?.totalSum?.toLong() ?: 0,
                (itemSummaryReport.hasValue && itemSummaryReport!!.totalSum.hasValue && itemSummaryReport.currency.hasValue)(
                    { Money(BigDecimal.valueOf(itemSummaryReport!!.totalSum ?: 0.0), Currency.valueOf(itemSummaryReport.currency!!)) },
                    { null }
                )
            )
        }

    private val selectedCategoriesMutable = MutableStateFlow(listOf<Category>())
    val selectedCategoriesReadonly = selectedCategoriesMutable.asStateFlow()

    private val editingCategoryErrorMutable = MutableStateFlow<ConditionalErrorMessage?>(null)
    val editingCategoryErrorReadOnly = editingCategoryErrorMutable.asStateFlow()

    companion object {
        fun clearQuestion() {


        }

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