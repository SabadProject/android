package farayan.sabad.commons

import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeResult

data class ExtractedBarcode(val text: String, val format: BarcodeFormat) {
    constructor(barcodeResult: BarcodeResult) : this(barcodeResult.text, barcodeResult.barcodeFormat)

    override fun toString(): String {
        return "$text (${format.name})"
    }
}

fun BarcodeResult.barcode() = ExtractedBarcode(this)