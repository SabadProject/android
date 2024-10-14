package farayan.sabad.repo

import android.util.Log
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import farayan.sabad.core.commons.Currency
import farayan.sabad.core.commons.UnitVariations
import farayan.sabad.db.Invoice
import farayan.sabad.db.Item
import farayan.sabad.db.ItemQueries
import farayan.sabad.db.ItemSummaryReport
import farayan.sabad.db.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.shareIn
import java.math.BigDecimal
import farayan.sabad.db.Unit as PersistenceUnit

class ItemRepo(private val queries: ItemQueries) {
    fun ensure(
        product: Product,
        price: BigDecimal?,
        currency: Currency?,
        quantity: BigDecimal,
        unit: PersistenceUnit?,
        packageWorth: BigDecimal?,
        packageUnit: UnitVariations?
    ): Item {
        val ii = current(product)
        return if (ii == null) {
            create(product, price, currency, quantity, unit, packageWorth, packageUnit)
        } else {
            update(ii, price, currency, quantity, unit, packageWorth, packageUnit)
        }
    }

    private fun update(
        invoiceItem: Item,
        price: BigDecimal?,
        currency: Currency?,
        quantity: BigDecimal,
        unit: PersistenceUnit?,
        packageWorth: BigDecimal?,
        packageUnit: UnitVariations?
    ): Item {
        queries.update(
            quantity.toString(),
            unit?.id,
            currency?.name,
            price.toString(),
            BigDecimal.ZERO.toString(),
            price?.multiply(quantity)?.toString(),
            packageWorth?.toString(),
            packageUnit?.name,
            invoiceItem.id
        )
        return queries.byId(invoiceItem.id).executeAsOne()
    }

    private fun create(
        product: Product,
        price: BigDecimal?,
        currency: Currency?,
        quantity: BigDecimal,
        unit: PersistenceUnit?,
        packageWorth: BigDecimal?,
        packageUnit: UnitVariations?
    ): Item {
        return queries.transactionWithResult {
            queries.create(
                product.categoryId,
                product.id,
                quantity.toString(),
                unit?.id,
                currency?.name,
                price?.toString(),
                BigDecimal.ZERO.toString(),
                price?.multiply(quantity)?.toString(),
                packageWorth?.toString(),
                packageUnit?.name
            )
            queries.created().executeAsOne()
        }
    }

    private fun current(product: Product): Item? {
        return queries.current(product.id).executeAsOneOrNull()
    }

    private val repoScope = CoroutineScope(Dispatchers.IO)

    private val _pickings: SharedFlow<List<Item>> = queries
        .pickings()
        .asFlow()
        .mapToList(Dispatchers.IO)
        .shareIn(repoScope, SharingStarted.WhileSubscribed(50_000), 1)

    fun pickingsFlow(): SharedFlow<List<Item>> = _pickings

    fun pickingsList(): List<Item> = queries.pickings().executeAsList()

    fun pendingItemByProduct(product: Product): Item? {
        return queries.byProductAndNullInvoice(product.id).executeAsOneOrNull()
    }

    fun delete(item: Item) {
        queries.delete(item.id)
    }

    fun currentCurrency(): Currency? {
        return queries.currentCurrency().executeAsOneOrNull()?.let { Currency.valueOf(it) }
    }

    fun checkout(invoice: Invoice) {
        queries.checkout(invoice.id)
    }

    fun byInvoice(invoice: Invoice): List<Item> {
        return queries.byInvoice(invoice.id).executeAsList()
    }

    val itemSummaryFlow: Flow<ItemSummaryReport?> = queries
        .itemSummaryReport()
        .asFlow()
        .catch { Log.i("itemSummary", "ERROR: ${it.message}") }
        .mapToOneOrNull(Dispatchers.IO)
        .catch { Log.i("itemSummary", "EXCEPTION: ${it.message}") }

    fun itemSummaryItem(): ItemSummaryReport? = queries
        .itemSummaryReport()
        .executeAsOneOrNull()

    val allFlow: Flow<List<Item>> = queries.all().asFlow().mapToList(Dispatchers.IO)
}