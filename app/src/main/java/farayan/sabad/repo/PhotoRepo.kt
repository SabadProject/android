package farayan.sabad.repo

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import farayan.sabad.db.Photo
import farayan.sabad.db.PhotoQueries
import farayan.sabad.db.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class PhotoRepo(private val queries: PhotoQueries) {
    fun create(product: Product, path: String): Photo {
        return queries.transactionWithResult {
            val position = 0L
            val widthPixels = 0L
            val heightPixels = 0L
            val widthHeightRatio = 0.0
            val sizeInBytes = 0L
            val type = ""
            queries.create(product.id, path, position, widthPixels, heightPixels, widthHeightRatio, sizeInBytes, type)
            queries.created().executeAsOne()
        }
    }

    fun ensure(product: Product, photo: String): Photo {
        return queries.byProductAndPath(product.id, photo).executeAsOneOrNull() ?: create(product, photo)
    }

    fun byProduct(product: Product): List<Photo> = queries.byProduct(product.id).executeAsList()
    val allFlow: Flow<List<Photo>> = queries.all().asFlow().mapToList(Dispatchers.IO)
}