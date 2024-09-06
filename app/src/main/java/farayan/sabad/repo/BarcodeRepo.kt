package farayan.sabad.repo

import farayan.sabad.commons.ExtractedBarcode
import farayan.sabad.db.Barcode
import farayan.sabad.db.BarcodeQueries
import farayan.sabad.db.Product

class BarcodeRepo(private val queries: BarcodeQueries) {
    fun create(product: Product, barcode: ExtractedBarcode): Barcode {
        return queries.transactionWithResult {
            queries.create(product.id, barcode.textual, barcode.format.name, null, null, null)
            queries.created().executeAsOne()
        }
    }

    fun ensure(product: Product, barcode: ExtractedBarcode): Barcode {
        val current = byBarcode(barcode)
        return current.firstOrNull { it.productId == product.id } ?: create(product, barcode)
    }

    fun byBarcode(barcode: ExtractedBarcode): List<Barcode> {
        return queries.byBarcode(barcode.textual, barcode.format.name).executeAsList()
    }

    fun byProduct(product: Product): List<Barcode> {
        return queries.byProduct(product.id).executeAsList()
    }
}