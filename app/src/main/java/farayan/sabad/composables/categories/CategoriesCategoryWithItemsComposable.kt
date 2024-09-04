package farayan.sabad.composables.categories

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import farayan.sabad.db.Category
import farayan.sabad.db.Item
import farayan.sabad.db.Product
import farayan.sabad.ui.ItemRich
import farayan.sabad.utility.hasValue
import farayan.sabad.utility.tryCatch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoriesCategoryWithItemsComposable(
    category: Category,
    items: List<Item>,
    changeNeeded: (Category, Boolean) -> Unit,
    products: List<Product>,
    units: List<farayan.sabad.db.Unit>,
    onRemove: (ItemRich) -> Unit,
    clickSelectMode: Boolean,
    selected: Boolean,
    onSelect: (Category) -> Unit,
    onUnselect: (Category) -> Unit,
) {
    val categoryItems = tryCatch({ items.filter { it.categoryId == category.id } }, listOf())
    val picked = categoryItems.isNotEmpty()
    val ctx = LocalContext.current
    Column(
        modifier = Modifier
            .background(categoryBackgroundRes(category.needed, picked, selected), shape = RoundedCornerShape(5.dp))
            .combinedClickable(
                onClick = {
                    if (clickSelectMode) {
                        if (selected) {
                            onUnselect(category)
                        } else {
                            onSelect(category)
                        }
                    } else {
                        if (!category.needed) {
                            changeNeeded(category, true)
                        } else {
                            displayCategoryDialog(category, ctx as Activity)
                        }
                    }
                },
                onLongClick = { onSelect(category) }
            )
            .fillMaxWidth()
    ) {
        CategoriesCategoryOnlyComposable(category, changeNeeded, picked, selected)
        for (item in categoryItems) {
            val product = products.firstOrNull { it.id == item.productId }
            val unit = item.unitId?.let { units.firstOrNull { it.id == item.unitId } }
            if (product.hasValue) {
                CategoriesCategoryPickedItem(
                    item,
                    product!!,
                    unit,
                    onEdit = { displayItemDialog(item, ctx as AppCompatActivity) },
                    onRemove = { onRemove(ItemRich(item, product, category)) },
                    Modifier.fillMaxWidth()
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(3.dp))
}


private fun categoryBackgroundRes(needed: Boolean, picked: Boolean, selected: Boolean): Color {
    return when {
        selected -> Color(0xFF, 0x7F, 0x00)
        needed && picked -> Color(0x5b, 0xc0, 0xde)
        needed -> Color(0x5c, 0xb8, 0x5c)
        else -> Color(0xf5, 0xf5, 0xf5)
    }
}