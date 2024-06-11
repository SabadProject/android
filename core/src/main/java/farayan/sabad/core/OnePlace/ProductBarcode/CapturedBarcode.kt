package farayan.sabad.core.OnePlace.ProductBarcode

import android.graphics.Bitmap

class CapturedBarcode(
        val text: String,
        val format: BarcodeFormats,
        val bitmapScaleFactor: Int,
        val bitmap: Bitmap,
        val resultPoints: Array<BarcodePoint>,
)