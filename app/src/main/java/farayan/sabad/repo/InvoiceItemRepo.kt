package farayan.sabad.repo

import farayan.sabad.core.commons.Currency
import farayan.sabad.core.commons.UnitVariations
import farayan.sabad.db.InvoiceItem
import farayan.sabad.db.InvoiceItemQueries
import farayan.sabad.db.Product
import java.math.BigDecimal
import farayan.sabad.db.Unit as PersistenceUnit

class InvoiceItemRepo(private val queries: InvoiceItemQueries) {
    fun ensure(
        product: Product,
        price: BigDecimal,
        currency: Currency,
        quantity: BigDecimal,
        unit: PersistenceUnit,
        packageWorth: BigDecimal?,
        packageUnit: UnitVariations?
    ): InvoiceItem {
        val ii = current(product)
        return if (ii == null) {
            create(product, price, currency, quantity, unit, packageWorth, packageUnit)
        } else {
            update(ii, price, currency, quantity, unit, packageWorth, packageUnit)
        }
    }

    private fun update(
        invoiceItem: InvoiceItem,
        price: BigDecimal,
        currency: Currency,
        quantity: BigDecimal,
        unit: PersistenceUnit,
        packageWorth: BigDecimal?,
        packageUnit: UnitVariations?
    ): InvoiceItem {
        queries.update(
            quantity.toString(),
            unit.id,
            currency.name,
            price.toString(),
            BigDecimal.ZERO.toString(),
            price.multiply(quantity).toString(),
            packageWorth?.toString(),
            packageUnit?.name,
            invoiceItem.id
        )
        return queries.byId(invoiceItem.id).executeAsOne()
    }

    private fun create(
        product: Product,
        price: BigDecimal,
        currency: Currency,
        quantity: BigDecimal,
        unit: PersistenceUnit,
        packageWorth: BigDecimal?,
        packageUnit: UnitVariations?
    ): InvoiceItem {
        return queries.transactionWithResult {
            queries.create(
                product.id,
                quantity.toString(),
                unit.id,
                currency.name,
                price.toString(),
                BigDecimal.ZERO.toString(),
                price.multiply(quantity).toString(),
                packageWorth?.toString(),
                packageUnit?.name
            )
            queries.created().executeAsOne()
        }
    }

    private fun current(product: Product): InvoiceItem? {
        return queries.current(product.id).executeAsOneOrNull()
    }

    fun pickedItems(): List<InvoiceItem> {
        return listOf()
    }

    fun pendingItemByProduct(product: Product): InvoiceItem? {
        return queries.byProductAndNullInvoice(product.id).executeAsOneOrNull()
    }
}