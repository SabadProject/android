package farayan.sabad.model.product_barcode

import android.content.Context
import com.journeyapps.barcodescanner.BarcodeResult
import farayan.commons.QueryBuilderCore.IRepo
import farayan.sabad.core.model.product.ProductEntity

interface IProductBarcodeRepo : IRepo<ProductBarcodeEntity> {
    fun ByProduct(product: ProductEntity): ProductBarcodeEntity?
    fun EnsureBarcodeRegistered(
        context: Context,
        barcodeResult: BarcodeResult,
        productEntity: ProductEntity
    ): ProductBarcodeEntity

    fun EnsureBarcodeRegistered(barcode: String, productEntity: ProductEntity): ProductBarcodeEntity
    fun ByBarcode(barcodeResult: QueryableBarcode): ProductEntity?
    fun byBarcode(barcodeResult: QueryableBarcode): List<ProductEntity>
}