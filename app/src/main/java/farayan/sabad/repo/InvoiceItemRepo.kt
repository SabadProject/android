package farayan.sabad.repo

import farayan.sabad.core.commons.Currency
import farayan.sabad.core.commons.UnitVariations
import farayan.sabad.db.Item
import farayan.sabad.db.ItemQueries
import farayan.sabad.db.Product
import java.math.BigDecimal
import farayan.sabad.db.Unit as PersistenceUnit

class InvoiceItemRepo(private val queries: ItemQueries) {
    fun ensure(
        product: Product,
        price: BigDecimal,
        currency: Currency,
        quantity: BigDecimal,
        unit: PersistenceUnit,
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
        price: BigDecimal,
        currency: Currency,
        quantity: BigDecimal,
        unit: PersistenceUnit,
        packageWorth: BigDecimal?,
        packageUnit: UnitVariations?
    ): Item {
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
    ): Item {
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

    private fun current(product: Product): Item? {
        return queries.current(product.id).executeAsOneOrNull()
    }

    fun pickedItems(): List<Item> {
        return listOf()
    }

    fun pendingItemByProduct(product: Product): Item? {
        return queries.byProductAndNullInvoice(product.id).executeAsOneOrNull()
    }
}