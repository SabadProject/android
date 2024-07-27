package farayan.sabad.model.product_barcode

import com.google.zxing.BarcodeFormat

data class QueryableBarcode(val value: String, val format: BarcodeFormat)