package farayan.sabad.ui.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import farayan.sabad.commons.InvoiceSummary
import farayan.sabad.commons.ItemRich
import farayan.sabad.db.Category
import farayan.sabad.core.commons.hasValue
import farayan.sabad.utility.isUsable
import farayan.sabad.vm.CategoriesViewModel
import farayan.sabad.vm.TopAppBarIcon
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoriesCategoriesComposable(categoriesViewModel: CategoriesViewModel, onAppBarIconChange: (List<TopAppBarIcon>) -> Unit, navController: NavHostController) {
    var selectedCategories by remember { mutableStateOf(listOf<Category>()) } // categoriesViewModel.selectedCategoriesReadonly.collectAsStateWithLifecycle()
    var editingCategory by remember { mutableStateOf<Category?>(null) }
    var checkingOut by remember { mutableStateOf(false) }
    val editingCategoryError by categoriesViewModel.editingCategoryErrorReadOnly.collectAsStateWithLifecycle()
    var removingCategories by remember { mutableStateOf<List<Category>>(listOf()) }

    val categories by categoriesViewModel.categories.collectAsStateWithLifecycle(listOf())
    val products by categoriesViewModel.pickedProducts.collectAsStateWithLifecycle(listOf())
    val units by categoriesViewModel.pickedUnits.collectAsStateWithLifecycle(listOf())
    val items by categoriesViewModel.pickedItems.collectAsStateWithLifecycle(listOf())
    val categoriesQuery by categoriesViewModel.queryReadOnly.collectAsStateWithLifecycle()
    val invoiceSummary by categoriesViewModel.invoiceSummary.collectAsStateWithLifecycle(InvoiceSummary(0, 0, 0, null))

    val refreshing by categoriesViewModel.refreshingReadOnly.collectAsStateWithLifecycle()
    val refreshState: PullRefreshState = rememberPullRefreshState(refreshing, onRefresh = { categoriesViewModel.refresh() })

    var removingItem by remember { mutableStateOf<ItemRich?>(null) }

    val editCategoryButton = TopAppBarIcon(Icons.Filled.Edit, "edit", { editingCategory = selectedCategories.first() }, 0)
    val deleteCategoriesButton = TopAppBarIcon(Icons.Filled.Delete, "delete", { removingCategories = selectedCategories }, 0)
    val checkoutButton = TopAppBarIcon(Icons.Filled.ShoppingCart, "checkout", { checkingOut = true }, 0)

    fun updateActionIcons() {
        val actionIcons = mutableListOf<TopAppBarIcon>()
        if (selectedCategories.size == 1) {
            actionIcons.add(editCategoryButton)
        }
        if (selectedCategories.isNotEmpty()) {
            actionIcons.add(deleteCategoriesButton)
        }
        if (items.isNotEmpty()) {
            actionIcons.add(checkoutButton)
        }
        onAppBarIconChange(actionIcons)
    }

    fun select(it: Category) {
        selectedCategories += it
        updateActionIcons()
    }

    fun unselect(it: Category) {
        selectedCategories -= it
        updateActionIcons()
    }

    Column(modifier = Modifier.padding(4.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
        ) {
            LaunchedEffect(key1 = items) {
                updateActionIcons()
            }
            LazyColumn(modifier = Modifier.pullRefresh(refreshState)) {
                items(categories, key = { "${it.id}@${it.updated}" }) { category -> // import androidx.compose.foundation.lazy.items
                    CategoriesCategoryWithItemsComposable(
                        category,
                        items,
                        { c, n -> categoriesViewModel.changeNeeded(c, n) },
                        products,
                        units,
                        { removingItem = it },
                        selectedCategories.isNotEmpty(),
                        selectedCategories.contains(category),
                        { /*categoriesViewModel.*/select(it); },
                        { /*categoriesViewModel.*/unselect(it); },
                    )
                }
            }
            PullRefreshIndicator(
                refreshing = false,
                state = refreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
        if (editingCategory.hasValue) {
            CategoriesCategoryEditComposable(
                editingCategory!!,
                editingCategoryError,
                {
                    if (categoriesViewModel.editCategory(it, editingCategory!!)) {
                        editingCategory = null
                        categoriesViewModel.clearSelection()
                    }
                },
                {
                    editingCategory = null;
                    categoriesViewModel.clearSelection()
                }
            )
        }
        if (removingCategories.isNotEmpty()) {
            CategoriesCategoriesDeleteComposable(
                removingCategories,
                {
                    if (categoriesViewModel.deleteCategories(removingCategories)) {
                        removingCategories = listOf();
                        categoriesViewModel.clearSelection()
                    }
                },
                {
                    removingCategories = listOf();
                    categoriesViewModel.clearSelection()
                }
            )
        }
        if (removingItem.hasValue) {
            CategoriesCategoryRemoteItemComposable(
                removingItem = removingItem!!,
                onConfirmed = { categoriesViewModel.removeItem(removingItem!!.item); removingItem = null },
                onCancelled = { removingItem = null }
            )
        }
        if (checkingOut) {
            CategoriesCheckoutComposable(
                onConfirmed = { shop -> categoriesViewModel.checkout(shop); checkingOut = false },
                onCancelled = { checkingOut = false }
            )
        }
        PurchaseSummaryComposable(invoiceSummary)
        CategoryQuickQueryComposable(
            addable = categoriesQuery.queryable.isUsable && categories.none { it.queryableName.contentEquals(categoriesQuery.queryable) },
            value = categoriesQuery.original,
            onValueChanged = {
                categoriesViewModel.filter(it)
            },
            onAddClicked = { categoriesViewModel.addCategory() }
        )
    }
}