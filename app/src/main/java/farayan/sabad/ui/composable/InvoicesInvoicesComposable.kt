package farayan.sabad.ui.composable

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.ibm.icu.util.Calendar
import farayan.sabad.R
import farayan.sabad.core.commons.Money
import farayan.sabad.core.commons.UnitVariations
import farayan.sabad.core.commons.hasValue
import farayan.sabad.core.commons.localize
import farayan.sabad.utility.PERSIAN_LOCALE
import farayan.sabad.utility.appFont
import farayan.sabad.utility.isUsable
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

    if (invoices.value.isEmpty()) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Text(text = stringResource(R.string.invoices_empty_invoices_label), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, style = appTextStyle)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
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
                    if(invoice.shop.isUsable) {
                        Text(text = stringResource(id = R.string.invoices_invoice_shop_label), style = appTextStyle)
                        Text(text = invoice.shop, style = boldTextStyle)
                        Spacer(modifier = horizontalSpacingModifier)
                    }

                    Text(text = stringResource(R.string.invoices_invoiec_date_label), style = appTextStyle)
                    Text(text = Date(invoice.moment).standard(), style = boldTextStyle)
                    Spacer(modifier = horizontalSpacingModifier)

                    Money.of(invoice.payable, invoice.currency)?.let {
                        Text(text = stringResource(R.string.invoices_invoice_amount_label), style = appTextStyle)
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
                    val category = categories.value.firstOrNull { c -> c.id == item.categoryId } ?: return@items
                    val product = products.value.firstOrNull { p -> p.id == item.productId } ?: return@items
                    val unit = item.unitId?.let { units.value.singleOrNull { u -> u.id == item.unitId } }
                    val quantity = BigDecimal(item.quantity)
                    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                        Text(text = "(${index + 1})", style = appTextStyle)
                        Spacer(modifier = horizontalSpacingModifier)
                        Text(text = category.displayableName + ":", style = boldTextStyle)
                        Spacer(modifier = horizontalSpacingModifier)
                        Text(text = product.displayableName, style = boldTextStyle)
                        Spacer(modifier = horizontalSpacingModifier)
                        Text(text = quantity.toString().localize(), style = boldTextStyle)
                        Spacer(modifier = horizontalSpacingModifier)
                        val unitName = unit?.displayableName ?: stringResource(id = R.string.invoices_invoice_unit_default)
                        Text(text = unitName, style = boldTextStyle)
                        Spacer(modifier = horizontalSpacingModifier)
                        if (item.packageUnit.hasValue && item.packageWorth.hasValue) {
                            Text(text = "(${item.packageWorth!!.localize()} ${stringResource(id = UnitVariations.valueOf(item.packageUnit!!).nameResId)})", style = appTextStyle)
                            Spacer(modifier = horizontalSpacingModifier)
                        }
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
                            Image(
                                painter = rememberAsyncImagePainter(photo.path),
                                contentDescription = "",
                                modifier = Modifier
                                    .width(144.dp)
                                    .height(144.dp)
                                    .padding(4.dp)
                            )
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