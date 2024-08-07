package farayan.sabad.repo

import farayan.sabad.db.Unit
import farayan.sabad.db.UnitQueries

class UnitRepo(private val queries: UnitQueries) {
    fun all(): List<Unit> {
        return queries.all().executeAsList()
    }

    fun byId(id: Long): Unit? = queries.byId(id).executeAsOneOrNull()
}