package farayan.sabad.repo

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import farayan.commons.queryable
import farayan.sabad.commons.Text
import farayan.sabad.db.Category
import farayan.sabad.db.CategoryQueries
import farayan.sabad.utility.displayable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import java.time.Instant

class CategoryRepo(private val queries: CategoryQueries) {
    fun all(): List<Category> {
        return queries.all().executeAsList()
    }

    fun allFlow(): Flow<List<Category>> = queries.all().asFlow().mapToList(Dispatchers.IO)

    fun allSharedFlow(scope: CoroutineScope): SharedFlow<List<Category>> = queries.all().asFlow().mapToList(Dispatchers.IO).shareIn(scope, SharingStarted.WhileSubscribed(5_000), 1)

    fun filterRaw(q: String): List<Category> {
        return queries.filter(q).executeAsList()
    }

    fun filterSharedFlow(q: String, scope: CoroutineScope): SharedFlow<List<Category>> =
        queries.filter(q).asFlow().mapToList(Dispatchers.IO).shareIn(scope, SharingStarted.WhileSubscribed(5_000), 1)

    fun filterFlow(q: String): Flow<List<Category>> =
        queries.filter(q).asFlow().mapToList(Dispatchers.IO)

    fun byId(id: Long): Category? {
        return queries.byId(id).executeAsOneOrNull()
    }

    fun byName(name: String): Category? {
        return queries.byName(name.queryable()).executeAsOneOrNull()
    }

    fun byName(name: Text): Category? {
        return queries.byName(name.queryable).executeAsOneOrNull()
    }

    fun changeNeeded(category: Category, it: Boolean): Category {
        queries.changeNeeded(it, category.id)
        return queries.byId(category.id).executeAsOne()
    }

    fun pickedCount(): Long {
        return queries.pickedCategoriesCount().executeAsOne()
    }

    fun remainingCount(): Long {
        return queries.remainedCategoriesCount().executeAsOne()
    }

    fun create(name: Text): Category {
        return queries.transactionWithResult {
            queries.create(name.displayable, name.queryable, "", "", true, 0)
            queries.created().executeAsOne()
        }
    }

    fun update(category: Category, name: String) {
        queries.updateName(name.displayable(), name.queryable(), category.id)
    }

    fun delete(removingCategories: List<Category>) {
        queries.softDelete(Instant.now().toEpochMilli().toString(), removingCategories.map { it.id })
    }
}