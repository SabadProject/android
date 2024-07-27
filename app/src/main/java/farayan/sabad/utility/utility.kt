package farayan.sabad.utility

import com.journeyapps.barcodescanner.BarcodeResult
import farayan.sabad.model.product_barcode.QueryableBarcode

val Any?.hasValue: Boolean
    get() {
        return this != null
    }

fun BarcodeResult.queryable(): QueryableBarcode {
    return QueryableBarcode(text, barcodeFormat)
}

