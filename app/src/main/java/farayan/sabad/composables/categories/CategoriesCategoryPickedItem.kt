package farayan.sabad.composables.categories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import farayan.sabad.core.commons.Currency
import farayan.sabad.core.commons.localize
import farayan.sabad.db.Item
import farayan.sabad.db.Product
import farayan.sabad.ui.appFont
import farayan.sabad.utility.invoke
import farayan.sabad.utility.isUsable
import java.math.BigDecimal

@Composable
fun CategoriesCategoryPickedItem(item: Item, product: Product, unit: farayan.sabad.db.Unit?, onEdit: (Item) -> Unit, onRemove: (Item) -> Unit, modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    Divider(color = Color.White, modifier = Modifier.padding(8.dp, 0.dp))
    Row(modifier = modifier
        .padding(5.dp)
        .clickable { onEdit(item) }) {
        Text(
            text = product.displayableName,
            modifier = Modifier
                .weight(1.0f)
                .padding(4.dp, 2.dp),
            style = TextStyle(fontFamily = appFont, color = Color.DarkGray)
        )
        Text(
            text = "${item.quantity.localize()} ${unit?.displayableName ?: ""}",
            modifier = Modifier
                .padding(4.dp, 2.dp)
                .width(64.dp),
            style = TextStyle(fontFamily = appFont, color = Color.DarkGray)
        )
        val currency = item.currency.isUsable({ Currency.valueOf(item.currency!!) }, { null })
        val price = currency?.let { currency.formatter(BigDecimal(item.fee), ctx) } ?: item.fee?.localize()

        Text(
            text = price ?: "",
            modifier = Modifier
                .padding(4.dp, 2.dp)
                .width(48.dp),
            style = TextStyle(fontFamily = appFont, color = Color.DarkGray)
        )
        Icon(
            Icons.Filled.Edit, contentDescription = "edit", tint = Color.White, modifier = Modifier
                .padding(4.dp, 2.dp)
                .width(16.dp)
                .height(16.dp)
        )
        Icon(
            Icons.Filled.Delete, contentDescription = "delete", tint = Color.White, modifier = Modifier
                .padding(4.dp, 2.dp)
                .width(16.dp)
                .height(16.dp)
                .clickable { onRemove(item) }
        )
    }
}