package farayan.sabad.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.primarySurface
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import farayan.sabad.R
import farayan.sabad.commons.InvoiceSummary
import farayan.sabad.ui.composable.CategoriesCategoriesDeleteComposable
import farayan.sabad.ui.composable.CategoriesCategoryEditComposable
import farayan.sabad.ui.composable.CategoriesCategoryRemoteItemComposable
import farayan.sabad.ui.composable.CategoriesCategoryWithItemsComposable
import farayan.sabad.ui.composable.CategoryQuickQueryComposable
import farayan.sabad.ui.composable.PurchaseSummaryComposable
import farayan.sabad.db.Category
import farayan.sabad.utility.hasValue
import farayan.sabad.utility.isUsable
import farayan.sabad.vm.HomeViewModel
import farayan.sabad.utility.appFont

class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterialApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val homeViewModel = HomeViewModel.Factory.create(HomeViewModel::class.java)
        setContent {
            CompositionLocalProvider(
                LocalLifecycleOwner provides this@MainActivity,
                LocalLayoutDirection provides LayoutDirection.Rtl,
                LocalContentAlpha provides ContentAlpha.medium
            ) {
                val selectedCategories by homeViewModel.selectedCategoriesReadonly.collectAsStateWithLifecycle()
                var editingCategory by remember { mutableStateOf<Category?>(null) }
                val editingCategoryError by homeViewModel.editingCategoryErrorReadOnly.collectAsStateWithLifecycle()
                var removingCategories by remember { mutableStateOf<List<Category>>(listOf()) }

                Scaffold(
                    topBar = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colors.primarySurface)
                                .height(48.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1.0f)
                                    .padding(8.dp, 4.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = stringResource(id = R.string.home_title), color = MaterialTheme.colors.onPrimary, fontFamily = appFont)
                            }
                            Row(
                                modifier = Modifier
                                    .padding(8.dp, 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (selectedCategories.isNotEmpty()) {
                                    val size = 32.dp
                                    if (selectedCategories.size == 1) {
                                        Icon(Icons.Filled.Edit, contentDescription = "edit", modifier = Modifier
                                            .clickable { editingCategory = selectedCategories.first() }
                                            .width(size)
                                            .height(size)
                                            .padding(4.dp, 0.dp),
                                            tint = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                    Icon(
                                        Icons.Filled.Delete, contentDescription = "delete", modifier = Modifier
                                            .width(size)
                                            .height(size)
                                            .padding(4.dp, 0.dp)
                                            .clickable {
                                                removingCategories = selectedCategories
                                            },
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    },
                ) {
                    val categories by homeViewModel.categories.collectAsStateWithLifecycle(listOf())
                    val products by homeViewModel.pickedProducts.collectAsStateWithLifecycle(listOf())
                    val units by homeViewModel.pickedUnits.collectAsStateWithLifecycle(listOf())
                    val items by homeViewModel.pickedItems.collectAsStateWithLifecycle(listOf())
                    val categoriesQuery by homeViewModel.queryReadOnly.collectAsStateWithLifecycle()
                    val invoiceSummary by homeViewModel.invoiceSummary.collectAsStateWithLifecycle(InvoiceSummary(0, 0, 0, null))

                    val refreshing by homeViewModel.refreshingReadOnly.collectAsStateWithLifecycle()
                    val refreshState: PullRefreshState = rememberPullRefreshState(refreshing, onRefresh = { homeViewModel.refresh() })

                    var removingItem by remember { mutableStateOf<ItemRich?>(null) }

                    Column(modifier = Modifier.padding(4.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1.0f)
                        ) {
                            LazyColumn(modifier = Modifier.pullRefresh(refreshState)) {
                                items(categories, key = { it.id }) { category -> // import androidx.compose.foundation.lazy.items
                                    CategoriesCategoryWithItemsComposable(
                                        category,
                                        items,
                                        { c, n -> homeViewModel.changeNeeded(c, n) },
                                        products,
                                        units,
                                        { removingItem = it },
                                        selectedCategories.isNotEmpty(),
                                        selectedCategories.contains(category),
                                        { homeViewModel.select(it) },
                                        { homeViewModel.unselect(it) },
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
                                    if (homeViewModel.editCategory(it, editingCategory!!)) {
                                        editingCategory = null
                                        homeViewModel.clearSelection()
                                    }
                                },
                                {
                                    editingCategory = null;
                                    homeViewModel.clearSelection()
                                }
                            )
                        }
                        if (removingCategories.isNotEmpty()) {
                            CategoriesCategoriesDeleteComposable(
                                removingCategories,
                                {
                                    if (homeViewModel.deleteCategories(removingCategories)) {
                                        removingCategories = listOf();
                                        homeViewModel.clearSelection()
                                    }
                                },
                                {
                                    removingCategories = listOf();
                                    homeViewModel.clearSelection()
                                }
                            )
                        }
                        if (removingItem.hasValue) {
                            CategoriesCategoryRemoteItemComposable(
                                removingItem = removingItem!!,
                                onConfirmed = { homeViewModel.removeItem(removingItem!!.item); removingItem = null },
                                onCancelled = { removingItem = null }
                            )
                        }
                        PurchaseSummaryComposable(invoiceSummary)
                        CategoryQuickQueryComposable(
                            addable = categoriesQuery.queryable.isUsable && categories.none { it.queryableName.contentEquals(categoriesQuery.queryable) },
                            value = categoriesQuery.original,
                            onValueChanged = {
                                homeViewModel.filter(it)
                            },
                            onAddClicked = { homeViewModel.addCategory() }
                        )
                    }
                }
            }
        }
    }
}
