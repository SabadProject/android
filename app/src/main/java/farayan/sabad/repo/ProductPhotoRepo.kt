package farayan.sabad.repo

import farayan.sabad.db.Photo
import farayan.sabad.db.PhotoQueries
import farayan.sabad.db.Product

class ProductPhotoRepo(private val queries: PhotoQueries) {
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
}