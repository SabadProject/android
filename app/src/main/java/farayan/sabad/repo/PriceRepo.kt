package farayan.sabad.repo

import farayan.sabad.db.Price
import farayan.sabad.db.PriceQueries
import farayan.sabad.db.Product

class PriceRepo(private val queries: PriceQueries) {
    fun last(product: Product): Price? {
        return queries.last(product.id).executeAsOneOrNull()
    }
}