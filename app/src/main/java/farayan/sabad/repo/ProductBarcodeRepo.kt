package farayan.sabad.repo

import farayan.sabad.commons.Barcode
import farayan.sabad.db.Product
import farayan.sabad.db.ProductBarcode
import farayan.sabad.db.ProductBarcodeQueries

class ProductBarcodeRepo(private val queries: ProductBarcodeQueries) {
    fun create(product: Product, barcode: Barcode): ProductBarcode {
        return queries.transactionWithResult {
            queries.create(product.id, barcode.textual, barcode.format.name, null, null, null)
            queries.created().executeAsOne()
        }
    }

    fun ensure(product: Product, barcode: Barcode): ProductBarcode {
        val current = byBarcode(barcode)
        return current.firstOrNull { it.productId == product.id } ?: create(product, barcode)
    }

    fun byBarcode(barcode: Barcode): List<ProductBarcode> {
        return queries.byBarcode(barcode.textual, barcode.format.name).executeAsList()
    }

    fun byProduct(product: Product): List<ProductBarcode> {
        return queries.byProduct(product.id).executeAsList()
    }
}