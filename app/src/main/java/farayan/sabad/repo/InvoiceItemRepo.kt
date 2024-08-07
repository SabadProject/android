package farayan.sabad.repo

import farayan.sabad.core.commons.Currency
import farayan.sabad.db.InvoiceItem
import farayan.sabad.db.InvoiceItemQueries
import farayan.sabad.db.Product
import java.math.BigDecimal

class InvoiceItemRepo(private val queries: InvoiceItemQueries) {
    fun ensure(product: Product, price: BigDecimal, currency: Currency, quantity: BigDecimal): InvoiceItem {
        val ii = current(product)
        return if (ii == null) {
            create(product, price, currency, quantity)
        } else {
            update(ii, price, currency, quantity)
        }
    }

    private fun update(invoiceItem: InvoiceItem, price: BigDecimal, currency: Currency, quantity: BigDecimal): InvoiceItem {
        queries.update(quantity.toString(), currency.name, price.toString(), BigDecimal.ZERO.toString(), price.multiply(quantity).toString(), invoiceItem.id)
        return queries.byId(invoiceItem.id).executeAsOne()
    }

    private fun create(product: Product, price: BigDecimal, currency: Currency, quantity: BigDecimal): InvoiceItem {
        return queries.transactionWithResult {
            queries.create(product.id, quantity.toString(), currency.name, price.toString(), BigDecimal.ZERO.toString(), price.multiply(quantity).toString())
            queries.created().executeAsOne()
        }
    }

    private fun current(product: Product): InvoiceItem? {
        return queries.current(product.id).executeAsOneOrNull()
    }

    fun pickedItems(): List<InvoiceItem> {
        return listOf()
    }
}