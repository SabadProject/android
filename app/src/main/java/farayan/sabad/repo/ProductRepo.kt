package farayan.sabad.repo

import farayan.sabad.db.Category
import farayan.sabad.db.Product
import farayan.sabad.db.ProductQueries
import farayan.sabad.displayable
import farayan.sabad.queryable

class ProductRepo(private val queries: ProductQueries) {
    fun create(name: String, category: Category): Product {
        return queries.transactionWithResult {
            queries.create(name.displayable(), name.queryable(), category.id)
            queries.created().executeAsOne()
        }
    }

    fun byIds(ids: List<Long>): List<Product> {
        TODO("Not yet implemented")
    }
}