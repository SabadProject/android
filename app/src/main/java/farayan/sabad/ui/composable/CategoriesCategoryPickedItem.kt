package farayan.sabad.ui.composable

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import farayan.sabad.R
import farayan.sabad.core.commons.Currency
import farayan.sabad.core.commons.Money
import farayan.sabad.core.commons.localize
import farayan.sabad.db.Item
import farayan.sabad.db.Product
import farayan.sabad.utility.appFont
import farayan.sabad.utility.hasValue
import farayan.sabad.utility.invoke
import farayan.sabad.utility.isUsable
import farayan.sabad.utility.or
import java.math.BigDecimal

@Composable
fun CategoriesCategoryPickedItem(item: Item, product: Product, unit: farayan.sabad.db.Unit?, onEdit: (Item) -> Unit, onRemove: (Item) -> Unit, modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    val textStyle = TextStyle(fontFamily = appFont, color = Color.DarkGray, fontSize = 10.sp)
    Divider(color = Color.White, modifier = Modifier.padding(8.dp, 0.dp))
    Row(modifier = modifier
        .padding(5.dp)
        .clickable { onEdit(item) }) {
        Text(
            text = product.displayableName.or(stringResource(id = R.string.category_item_name_default)),
            modifier = Modifier
                .weight(1.0f)
                .padding(4.dp, 2.dp),
            style = textStyle
        )
        Text(
            text = "${item.quantity.localize()} ${unit?.displayableName.or(stringResource(id = R.string.category_item_unit_default))}",
            modifier = Modifier
                .padding(4.dp, 2.dp)
                .width(64.dp),
            style = textStyle
        )
        val currency = item.currency.isUsable({ Currency.valueOf(item.currency!!) }, { null })
        val fee = item.fee?.let { BigDecimal(it) }
        val feeCurrency = if (currency.hasValue && fee.hasValue) Money(fee!!, currency!!) else null
        val price = feeCurrency?.textual(ctx) ?: item.fee?.localize() ?: stringResource(id = R.string.category_item_price_default)

        Text(
            text = price,
            modifier = Modifier
                .padding(4.dp, 2.dp)
                .width(48.dp),
            style = textStyle
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