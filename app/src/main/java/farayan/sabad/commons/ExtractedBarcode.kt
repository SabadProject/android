package farayan.sabad.commons

import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeResult

data class ExtractedBarcode(val textual: String, val format: BarcodeFormat) {
    constructor(barcodeResult: BarcodeResult) : this(barcodeResult.text, barcodeResult.barcodeFormat)
}

fun BarcodeResult.barcode() = ExtractedBarcode(this)