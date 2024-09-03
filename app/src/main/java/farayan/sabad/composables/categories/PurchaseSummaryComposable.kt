package farayan.sabad.composables.categories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import farayan.sabad.R
import farayan.sabad.commons.InvoiceSummary
import farayan.sabad.core.commons.localize
import farayan.sabad.ui.appFont
import farayan.sabad.utility.hasValue

@Composable
fun PurchaseSummaryComposable(summary: InvoiceSummary) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1.0f)) {
            Text(text = stringResource(id = R.string.purchase_summary_picked_items_count_label), style = TextStyle(fontFamily = appFont, color = Color.Gray))
            Text(
                text = stringResource(
                    id = R.string.purchase_summary_picked_items_count_value,
                    summary.pickedItemsCount.toString().localize(),
                    summary.pickedQuantitiesSum.toString().localize()
                ),
                style = TextStyle(fontFamily = appFont, color = Color.Gray)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1.0f)) {
            Text(text = stringResource(id = R.string.purchase_summary_remained_items_count_label), style = TextStyle(fontFamily = appFont, color = Color.Gray))
            Text(
                text = stringResource(id = R.string.purchase_summary_remained_items_count_value, summary.remainedItemsCount.toString().localize()),
                style = TextStyle(fontFamily = appFont, color = Color.Gray)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1.0f)) {
            Text(text = stringResource(id = R.string.purchase_summary_price_label), style = TextStyle(fontFamily = appFont, color = Color.Gray))
            if (summary.price.hasValue) {
                Text(text = summary.price!!.currency.formatter(summary.price.amount, LocalContext.current), style = TextStyle(fontFamily = appFont, color = Color.Gray))
            }
        }
    }
}