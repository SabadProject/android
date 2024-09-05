package farayan.sabad.repo

import android.util.Log
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import farayan.sabad.core.commons.Currency
import farayan.sabad.core.commons.UnitVariations
import farayan.sabad.db.Item
import farayan.sabad.db.ItemQueries
import farayan.sabad.db.PickingsSummary
import farayan.sabad.db.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
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
            price?.multiply(quantity).toString(),
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
                price.toString(),
                BigDecimal.ZERO.toString(),
                price?.multiply(quantity).toString(),
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

    fun pickings(scope: CoroutineScope): SharedFlow<List<Item>> = _pickings

    private val _pickings: SharedFlow<List<Item>> = queries.pickings().asFlow().mapToList(Dispatchers.IO).onEach { items ->
        // Log each emission
        Log.d("flow", "Emitted items: ${items.size}")
    }.shareIn(repoScope, SharingStarted.WhileSubscribed(500_000), 1)


    fun pendingItemByProduct(product: Product): Item? {
        return queries.byProductAndNullInvoice(product.id).executeAsOneOrNull()
    }

    fun delete(item: Item) {
        queries.delete(item.id)
    }

    fun pickingSummary(): Flow<PickingsSummary?> {
        return queries.pickingsSummary().asFlow().mapToOneOrNull(Dispatchers.IO).onEach {
            Log.d("flow", "new summary: ${it}")
        }
    }
}