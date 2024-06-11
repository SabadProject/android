package farayan.sabad.core.OnePlace.ProductBarcode

import android.content.Context
import farayan.commons.QueryBuilderCore.IRepo
import farayan.sabad.core.OnePlace.Product.ProductEntity

interface IProductBarcodeRepo : IRepo<ProductBarcodeEntity> {
    fun ByProduct(product: ProductEntity): ProductBarcodeEntity?
    fun EnsureBarcodeRegistered(context: Context, barcodeResult: CapturedBarcode, productEntity: ProductEntity): ProductBarcodeEntity
    fun EnsureBarcodeRegistered(barcode: String, productEntity: ProductEntity): ProductBarcodeEntity
    fun ByBarcode(barcodeResult: CapturedBarcode): ProductEntity?
}