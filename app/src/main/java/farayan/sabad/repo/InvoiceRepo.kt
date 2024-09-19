package farayan.sabad.repo

import android.util.Log
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import farayan.sabad.core.commons.Currency
import farayan.sabad.core.commons.UnitVariations
import farayan.sabad.db.Invoice
import farayan.sabad.db.InvoiceQueries
import farayan.sabad.db.Item
import farayan.sabad.db.ItemQueries
import farayan.sabad.db.ItemSummaryReport
import farayan.sabad.db.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import org.joda.time.Instant
import java.math.BigDecimal
import farayan.sabad.db.Unit as PersistenceUnit

class InvoiceRepo(private val queries: InvoiceQueries) {
    fun create(shop: String, now: Instant, size: Long, currency: Currency?, subtotal: BigDecimal, discount: BigDecimal, payable: BigDecimal): Invoice {
        return queries.transactionWithResult {
            queries.create(shop, now.millis, size, currency?.name, subtotal.toString(), discount.toString(), payable.toString())
            queries.created().executeAsOne()
        }
    }

    val allFlow: Flow<List<Invoice>> = queries.all().asFlow().mapToList(Dispatchers.IO)
}