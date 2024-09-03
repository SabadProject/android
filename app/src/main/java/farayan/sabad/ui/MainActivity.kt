package farayan.sabad.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import farayan.sabad.R
import farayan.sabad.composables.categories.CategoriesCategoryRemoteItemComposable
import farayan.sabad.composables.categories.CategoriesCategoryWithItemsComposable
import farayan.sabad.composables.categories.CategoryQuickQueryComposable
import farayan.sabad.composables.categories.PurchaseSummaryComposable
import farayan.sabad.utility.hasValue
import farayan.sabad.utility.isUsable
import farayan.sabad.vms.HomeViewModel
import java.time.LocalDateTime

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
                                    .fillMaxSize()
                                    .padding(8.dp, 4.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = stringResource(id = R.string.home_title), color = MaterialTheme.colors.onPrimary, fontFamily = appFont)
                            }
                        }
                    },
                ) {
                    val categories by homeViewModel.categories.collectAsState(listOf())
                    val products by homeViewModel.pickedProducts.collectAsState(listOf())
                    val units by homeViewModel.pickedUnits.collectAsState(listOf())
                    val items by homeViewModel.pickedItems.collectAsState(listOf())
                    var removingItem by remember { mutableStateOf<ItemRich?>(null) }
                    val categoriesQuery by homeViewModel.categoriesQuery.collectAsState()
                    val refreshing by homeViewModel.refreshing.collectAsState()
                    val refreshState: PullRefreshState = rememberPullRefreshState(refreshing, onRefresh = { homeViewModel.refresh() })

                    Column(modifier = Modifier.padding(4.dp)) {
                        Text(
                            text = "cats: ${categories.size}, products: ${products.size}, items: ${items.size}. now: ${
                                LocalDateTime.now().let { it.minute.toString() + ":" + it.second }
                            }",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Left
                        )
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
                                        homeViewModel
                                    )
                                }
                            }
                            PullRefreshIndicator(
                                refreshing = false,
                                state = refreshState,
                                modifier = Modifier.align(Alignment.TopCenter)
                            )
                        }
                        if (removingItem.hasValue) {
                            CategoriesCategoryRemoteItemComposable(
                                removingItem = removingItem!!,
                                onConfirmed = { homeViewModel.removeItem(removingItem!!.item); removingItem = null },
                                onCancelled = { removingItem = null }
                            )
                        }
                        PurchaseSummaryComposable(homeViewModel.invoiceSummary())
                        CategoryQuickQueryComposable(
                            addable = categoriesQuery.queryable.isUsable && categories.none { it.queryableName.contentEquals(categoriesQuery.queryable) },
                            value = categoriesQuery.original,
                            onValueChanged = { homeViewModel.filter(it) },
                            onAddClicked = { homeViewModel.addCategory() }
                        )
                    }
                }
            }
        }
    }
}
