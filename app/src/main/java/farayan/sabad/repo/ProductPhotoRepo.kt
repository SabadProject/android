package farayan.sabad.repo

import farayan.sabad.db.Product
import farayan.sabad.db.ProductPhoto
import farayan.sabad.db.ProductPhotoQueries

class ProductPhotoRepo(private val queries: ProductPhotoQueries) {
    fun create(product: Product, path: String): ProductPhoto {
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

    fun ensure(product: Product, photo: String): ProductPhoto {
        return queries.byProductAndPath(product.id, photo).executeAsOneOrNull() ?: create(product, photo)
    }
}