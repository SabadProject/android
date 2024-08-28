package farayan.sabad.repo

import farayan.sabad.db.Category
import farayan.sabad.db.Product
import farayan.sabad.db.ProductQueries
import farayan.sabad.displayable
import farayan.sabad.queryable

class ProductRepo(private val queries: ProductQueries) {
    fun create(category: Category, name: String): Product {
        return queries.transactionWithResult {
            queries.create(name.displayable(), name.queryable(), category.id)
            queries.created().executeAsOne()
        }
    }

    fun byIds(ids: List<Long>): List<Product> {
        return queries.byIds(ids).executeAsList()
    }

    fun pickings(): List<Product> {
        return queries.pickings().executeAsList()
    }

    fun byId(id: Long): Product? {
        return queries.byId(id).executeAsOneOrNull()
    }

    private fun byCategoryAndName(category: Category, name: String): Product? {
        return queries.byCategoryAndName(category.id, name.queryable()).executeAsOneOrNull()
    }

    fun ensure(category: Category, name: String): Product {
        return byCategoryAndName(category, name) ?: create(category, name)
    }
}