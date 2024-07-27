package farayan.sabad.model.product_barcode

import com.google.zxing.BarcodeFormat
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import farayan.sabad.core.base.SabadEntityBase
import farayan.sabad.core.model.product.ProductEntity

@DatabaseTable(tableName = "product-barcodes")
class ProductBarcodeEntity : SabadEntityBase<ProductBarcodeEntity>() {
    @DatabaseField(columnName = "bitmap-result-points")
    var bitmapResultPoints: String? = null

    @DatabaseField(columnName = "bitmap-scale-factor")
    var bitmapScaleFactor: Int = 0

    @DatabaseField(columnName = "bitmap-file")
    var bitmapFile: String? = null

    @DatabaseField(columnName = "value")
    var value: String? = null

    @DatabaseField(columnName = "format")
    var format: BarcodeFormat? = null

    @DatabaseField(columnName = "product", foreign = true)
    var product: ProductEntity? = null
    override fun NeedsRefresh(): Boolean {
        return false
    }
}