package farayan.sabad.repo

import farayan.sabad.core.commons.Currency
import farayan.sabad.db.InvoiceItem
import farayan.sabad.db.InvoiceItemQueries
import farayan.sabad.db.Product
import java.math.BigDecimal

class InvoiceItemRepo(private val queries: InvoiceItemQueries) {
    fun ensure(product: Product, price: BigDecimal?, currency: Currency, quantity: BigDecimal): InvoiceItem {
        TODO("Not yet implemented")
    }

    fun pickedItems(): List<InvoiceItem> {
        return listOf()
    }
}