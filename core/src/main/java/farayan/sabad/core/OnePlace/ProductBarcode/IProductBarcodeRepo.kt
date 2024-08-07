package farayan.sabad.core.OnePlace.ProductBarcode

import android.content.Context
import farayan.commons.QueryBuilderCore.IRepo
import farayan.sabad.core.model.product.ProductEntity

interface IProductBarcodeRepo : IRepo<ProductBarcodeEntity> {
    fun ByProduct(product: ProductEntity): ProductBarcodeEntity?
    fun EnsureBarcodeRegistered(
        context: Context,
        barcodeResult: CapturedBarcode,
        productEntity: ProductEntity
    ): ProductBarcodeEntity

    fun EnsureBarcodeRegistered(barcode: String, productEntity: ProductEntity): ProductBarcodeEntity
    fun ByBarcode(barcodeResult: CapturedBarcode): ProductEntity?
    fun byBarcode(barcodeResult: CapturedBarcode): List<ProductEntity>
}