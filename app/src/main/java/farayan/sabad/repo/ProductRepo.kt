package farayan.sabad.repo

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import farayan.sabad.commons.Text
import farayan.sabad.db.Category
import farayan.sabad.db.Product
import farayan.sabad.db.ProductQueries
import farayan.sabad.utility.displayable
import farayan.sabad.utility.queryable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn

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

    fun pickingsFlow(scope: CoroutineScope): SharedFlow<List<Product>> =
        queries.pickings().asFlow().mapToList(Dispatchers.IO).shareIn(scope, SharingStarted.WhileSubscribed(5_000), 1)

    fun byId(id: Long): Product? {
        return queries.byId(id).executeAsOneOrNull()
    }

    private fun byCategoryAndName(category: Category, name: String): Product? {
        return queries.byCategoryAndName(category.id, name.queryable()).executeAsOneOrNull()
    }

    fun ensure(category: Category, name: String): Product {
        return byCategoryAndName(category, name) ?: create(category, name)
    }

    fun updateName(product: Product, name: Text) {
        queries.updateName(name.displayable,name.queryable, product.id)
    }
}