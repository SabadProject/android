package farayan.sabad.composables

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
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
import farayan.sabad.ui.InvoiceItemFormDialog
import farayan.sabad.ui.ItemRich
import farayan.sabad.vms.InvoiceItemFormViewModel
import farayan.sabad.vms.InvoiceItemFormViewModel.Companion.Factory

@Composable
fun CategoriesCategoryWithItemsComposable(
    category: Category,
    items: List<Item>,
    changeNeeded: (Category, Boolean) -> Unit,
    products: List<Product>,
    units: List<farayan.sabad.db.Unit>,
    onRemove: (ItemRich) -> Unit
) {
    val categoryItems = items.filter { it.categoryId == category.id }
    val picked = categoryItems.isNotEmpty()
    Column(
        modifier = Modifier
            .background(categoryBackgroundRes(category.needed, picked), shape = RoundedCornerShape(5.dp))
            .fillMaxWidth()
    ) {
        val ctx = LocalContext.current
        CategoriesCategoryOnlyComposable(category, changeNeeded, picked)
        for (item in categoryItems) {
            val product = products.first { it.id == item.productId }
            val unit = item.unitId?.let { units.firstOrNull { it.id == item.unitId } }
            CategoriesCategoryPickedItem(
                item,
                product,
                unit,
                onEdit = { displayItemDialog(item, ctx as AppCompatActivity) },
                onRemove = { onRemove(ItemRich(item, product, category)) },
                Modifier.fillMaxWidth()
            )
        }
    }
    Spacer(modifier = Modifier.height(3.dp))
}

private fun displayItemDialog(item: Item, activity: AppCompatActivity) {
    val dialogViewModel = Factory.create(InvoiceItemFormViewModel::class.java)
    val dialog = InvoiceItemFormDialog(
        activity,
        true,
        null,
        dialogViewModel
    )
    dialog.show()
    dialog.maximize()
    dialogViewModel.init(item)
}

private fun categoryBackgroundRes(needed: Boolean, picked: Boolean): Color {
    return if (needed) {
        if (picked) Color(0x5b, 0xc0, 0xde) else Color(0x5c, 0xb8, 0x5c)
    } else {
        Color(0xf5, 0xf5, 0xf5)
    }
}