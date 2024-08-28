package farayan.sabad.repo

import farayan.sabad.db.Category
import farayan.sabad.db.CategoryQueries

class CategoryRepo(private val queries: CategoryQueries) {
    fun all(): List<Category> {
        return queries.all().executeAsList()
    }

    fun filter(q: String): List<Category> {
        return queries.filter(q).executeAsList()
    }

    fun byId(id: Long): Category? {
        return queries.byId(id).executeAsOneOrNull()
    }

    fun changeNeeded(category: Category, it: Boolean): Category {
        queries.changeNeeded(it, category.id)
        return queries.byId(category.id).executeAsOne()
    }
}