package farayan.sabad.repo

import com.journeyapps.barcodescanner.BarcodeResult
import farayan.sabad.db.Product
import farayan.sabad.db.ProductBarcode
import farayan.sabad.db.ProductBarcodeQueries

class ProductBarcodeRepo(private val queries: ProductBarcodeQueries) {
    fun create(product: Product, barcode: BarcodeResult): ProductBarcode {
        return queries.transactionWithResult {
            queries.create(product.id, barcode.text, barcode.barcodeFormat.name, null, barcode.bitmapScaleFactor.toLong(), null)
            queries.created().executeAsOne()
        }
    }

    fun ensure(product: Product, barcode: BarcodeResult): ProductBarcode {
        val current = byBarcode(barcode)
        return current.firstOrNull { it.productId == product.id } ?: create(product, barcode)
    }

    fun byBarcode(barcode: BarcodeResult): List<ProductBarcode> {
        return queries.byBarcode(barcode.text, barcode.barcodeFormat.name).executeAsList()
    }
}