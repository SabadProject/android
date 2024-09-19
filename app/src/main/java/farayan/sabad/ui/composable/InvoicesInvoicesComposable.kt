package farayan.sabad.ui.composable

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ibm.icu.util.Calendar
import farayan.sabad.R
import farayan.sabad.core.commons.Money
import farayan.sabad.core.commons.hasValue
import farayan.sabad.core.commons.localize
import farayan.sabad.utility.PERSIAN_LOCALE
import farayan.sabad.utility.standard
import farayan.sabad.vm.InvoicesViewModel
import java.math.BigDecimal
import java.util.Date

@Composable
fun InvoicesInvoicesComposable(invoicesViewModel: InvoicesViewModel) {
    val invoices = invoicesViewModel.invoices.collectAsStateWithLifecycle(initialValue = listOf())
    val items = invoicesViewModel.items.collectAsStateWithLifecycle(initialValue = listOf())
    val categories = invoicesViewModel.categories.collectAsStateWithLifecycle(initialValue = listOf())
    val photos = invoicesViewModel.photos.collectAsStateWithLifecycle(initialValue = listOf())
    val products = invoicesViewModel.products.collectAsStateWithLifecycle(initialValue = listOf())
    val units = invoicesViewModel.units.collectAsStateWithLifecycle(initialValue = listOf())
    val ctx = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    )
    {
        val horizontalSpacingModifier = Modifier.padding(0.dp, 0.dp, 4.dp, 0.dp)
        items(invoices.value) { invoice ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, RoundedCornerShape(4.dp))
                    .padding(7.dp),
            ) {
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    Text(text = stringResource(id = R.string.invoices_invoice_shop_label), style = appTextStyle)
                    Text(text = invoice.shop, style = boldTextStyle)
                    Spacer(modifier = horizontalSpacingModifier)

                    Text(text = "در تاریخ: ", style = appTextStyle)
                    Text(text = Date(invoice.moment).standard(), style = boldTextStyle)
                    Spacer(modifier = horizontalSpacingModifier)

                    Money.of(invoice.payable, invoice.currency)?.let {
                        Text(text = "به مبلغ: ", style = appTextStyle)
                        Text(text = Money.of(invoice.payable, invoice.currency)?.textual(ctx) ?: "", style = boldTextStyle)
                        Spacer(modifier = horizontalSpacingModifier)
                    }
                }
                Spacer(
                    modifier = Modifier
                        .padding(4.dp)
                        .background(Color.White)
                        .fillMaxWidth()
                        .height(1.dp)
                )
                items.value.filter { item -> item.invoiceId == invoice.id }.forEachIndexed() { index, item ->
                    val product = products.value.firstOrNull { p -> p.id == item.productId } ?: return@items
                    val unit = item.unitId?.let { units.value.singleOrNull { u -> u.id == item.unitId } }
                    if (item.unitId.hasValue && unit == null) {
                        Log.i("debug-unit", "unit with id ${item.unitId} not found")
                    }
                    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                        val quantity = BigDecimal(item.quantity)
                        Text(text = "(${index + 1})", style = appTextStyle)
                        Spacer(modifier = horizontalSpacingModifier)
                        Text(text = product.displayableName, style = boldTextStyle)
                        Spacer(modifier = horizontalSpacingModifier)
                        Text(text = quantity.toString().localize(), style = boldTextStyle)
                        Spacer(modifier = horizontalSpacingModifier)
                        val unitName = unit?.displayableName ?: stringResource(id = R.string.invoices_invoice_unit_default)
                        Text(text = unitName, style = boldTextStyle)
                        Spacer(modifier = horizontalSpacingModifier)
                        val fee = Money.of(item.fee, item.currency)
                        if (fee.hasValue) {
                            if (quantity == BigDecimal.ONE) {
                                Text(text = fee!!.textual(ctx), style = boldTextStyle)
                            } else {
                                Text(text = stringResource(R.string.invoices_invoice_fee_label, unitName), style = appTextStyle)
                                Text(text = fee!!.textual(ctx), style = boldTextStyle)
                                Spacer(modifier = horizontalSpacingModifier)
                                Text(text = stringResource(R.string.invoices_invoice_total_label), style = appTextStyle)
                                Text(text = Money.of(item.total, item.currency)!!.textual(ctx), style = boldTextStyle)
                                Spacer(modifier = horizontalSpacingModifier)
                            }
                        }
                    }
                    LazyRow {
                        items(photos.value.filter { p -> p.productId == product.id }) { photo ->
                            Text(text = photo.path)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.padding(2.dp))
        }
    }
}

fun fromDateToPersianCalendar(date: Date?): Calendar {
    val persianCalendar: Calendar = Calendar.getInstance(PERSIAN_LOCALE)
    persianCalendar.clear()
    persianCalendar.time = date
    return persianCalendar
}