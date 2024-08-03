package farayan.sabad.repo

import farayan.sabad.db.Category
import farayan.sabad.db.CategoryQueries

class CategoryRepo(private val queries: CategoryQueries) {
    fun all(): List<Category> {
        return queries.all().executeAsList()
    }

    fun first(): Category? = queries.first().executeAsOneOrNull()
}