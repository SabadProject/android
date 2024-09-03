package farayan.sabad.repo

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import farayan.sabad.db.Unit
import farayan.sabad.db.UnitQueries
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn

class UnitRepo(private val queries: UnitQueries) {
    fun all(): List<Unit> {
        return queries.all().executeAsList()
    }

    fun byId(id: Long): Unit? = queries.byId(id).executeAsOneOrNull()
    fun pickings(): List<Unit> = queries.pickings().executeAsList()
    fun pickingsFlow(scope: CoroutineScope): SharedFlow<List<Unit>> = queries.pickings().asFlow().mapToList(Dispatchers.IO).shareIn(scope, SharingStarted.WhileSubscribed(5_000), 1)
}